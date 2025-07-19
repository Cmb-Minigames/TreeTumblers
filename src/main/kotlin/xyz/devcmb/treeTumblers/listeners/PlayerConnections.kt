package xyz.devcmb.treeTumblers.listeners

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.client.UIManager
import xyz.devcmb.treeTumblers.data.DataManager
import xyz.devcmb.treeTumblers.data.DataManager.TeamData
import xyz.devcmb.treeTumblers.util.Database
import xyz.devcmb.treeTumblers.util.Format.formatPlayerName
import xyz.devcmb.treeTumblers.util.Format.stringToUnicode
import java.util.stream.Collectors

class PlayerConnections : Listener {
    var headerTasks: MutableMap<Player, BukkitTask> = HashMap()

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage(
            Component.empty().append(
                Component.empty()
                    .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                    .append(Component.text("+").color(NamedTextColor.GREEN))
                    .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                    .append(formatPlayerName(event.getPlayer()))
            )
        )

        val player: Player = event.player
        DataManager.playerJoin(player)
        UIManager.playerAdded(player)

        Bukkit.getOnlinePlayers().forEach { plr: Player? ->
            player.unlistPlayer(plr!!)
            plr.unlistPlayer(player)
        }

        val task = Bukkit.getScheduler().runTaskTimer(TreeTumblers.plugin, Runnable {
            val components = arrayOf<Component?>(
                Component.newline()
                    .appendNewline()
                    .append(
                        Component.text("   \u0001   ")
                            .font(Key.key("tumbling:icons"))
                    ).appendNewline().appendNewline().appendNewline().appendNewline()
            )
            // Thank you copilot
            // This scares me :D
            val sortedTeamData: MutableMap<String, TeamData> = DataManager.teamData.entries.stream()
                .sorted(Comparator.comparingInt<Map.Entry<String, TeamData>> { e -> e.value.order })
                .collect(
                    Collectors.toMap(
                        { entry -> entry.key },
                        { entry -> entry.value },
                        { e1, _ -> e1 },
                        ::LinkedHashMap
                    )
                )

            sortedTeamData.forEach { (_: String?, data: TeamData) ->
                val playerComponents: MutableList<Component?> = ArrayList<Component?>()
                data.players.forEach({ plr ->
                    val name: String? = plr.name
                    playerComponents.add(
                        Component.text(
                            (name ?: Database.getDatabasePlayerName(plr.uniqueId.toString()))
                        ).color(if (plr.isOnline) TextColor.fromHexString(data.color) else NamedTextColor.DARK_GRAY)
                    )
                })

                val joinedComponents = Component.join(
                    JoinConfiguration.builder().separator(Component.text(" • ").color(NamedTextColor.WHITE)).build(),
                    playerComponents
                )
                components[0] = components[0]!!.append(
                    Component.newline().append(Component.newline()).append(Component.text("    ")).append(
                        Component.empty()
                            .append(Component.text(stringToUnicode(data.icon) + " ").font(Key.key("tumbling:icons")))
                            .append(joinedComponents)
                    ).append(Component.text("    "))
                )
            }

            components[0] = components[0]!!.appendNewline().appendNewline()
            player.sendPlayerListHeader(components[0]!!)
        }, 0, 10)

        headerTasks.put(player, task)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        UIManager.playerRemoved(event.player)
        DataManager.playerLeave(event.player)

        event.quitMessage(
            Component.empty().append(
                Component.empty()
                    .append(Component.text("[").color(NamedTextColor.DARK_GRAY))
                    .append(Component.text("-").color(NamedTextColor.RED))
                    .append(Component.text("] ").color(NamedTextColor.DARK_GRAY))
                    .append(formatPlayerName(event.getPlayer()))
            )
        )

        headerTasks[event.player]?.cancel()
    }
}
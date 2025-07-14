package xyz.devcmb.treeTumblers.commands.cmd

import kotlinx.coroutines.runBlocking
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import xyz.devcmb.treeTumblers.TreeTumblers.Companion.plugin
import xyz.devcmb.treeTumblers.client.UIManager
import xyz.devcmb.treeTumblers.data.DataManager
import xyz.devcmb.treeTumblers.util.Format

class PlayerDataCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(args.isEmpty()) {
            if(sender is Player) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
                    runBlocking {
                        UIManager.delegates[sender.player]!!.setInventory("playerDataInventory")
                    }
                })
            } else {
                sender.sendMessage(Format.format("Usage: /playerdata <player>", Format.FormatType.INVALID))
            }

            return true
        }

        val player: Player? = Bukkit.getPlayer(args[0])
        if(player == null) {
            sender.sendMessage(Format.format("Player is not online or does not exist", Format.FormatType.INVALID))
            return true
        }

        val data: DataManager.PlayerData? = DataManager.playerData[player]
        if(data == null) {
            sender.sendMessage(Format.format("Player does not have any player data. Report this to whoever necessary.", Format.FormatType.ERROR))
            return true
        }

        val comp: Component = Component.empty()
            .append(Component.translatable("%nox_uuid%${player.uniqueId},false,0,0,1.0","")) // Noxesium head (looks a bit nicer)
            .append(Component.text(" ${player.name}").color(NamedTextColor.GOLD))
            .append(Component.newline())
            .append(Component.newline())
            .append(Component.text("Team: ").color(NamedTextColor.AQUA))
            .append(Component.text(data.team ?: "spectator"))
            .append(Component.newline())
            .append(Component.text("Score: ").color(NamedTextColor.YELLOW))
            .append(Component.text(data.score))
            .append(Component.newline())
            .append(Component.text("Next autosave in ").color(NamedTextColor.RED))
            .append(Component.text(DataManager.getNextAutosaveTime()))

        sender.sendMessage(comp)

        return true
    }
}
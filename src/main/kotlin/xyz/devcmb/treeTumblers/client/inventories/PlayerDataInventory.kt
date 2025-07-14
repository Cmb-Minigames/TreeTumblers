package xyz.devcmb.treeTumblers.client.inventories

import com.noxcrew.interfaces.drawable.Drawable
import com.noxcrew.interfaces.element.StaticElement
import com.noxcrew.interfaces.interfaces.buildChestInterface
import com.noxcrew.interfaces.properties.interfaceProperty
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.data.DataManager
import xyz.devcmb.treeTumblers.util.Format
import xyz.devcmb.treeTumblers.util.Utilities.description
import xyz.devcmb.treeTumblers.util.Utilities.name
import kotlin.collections.forEachIndexed
import kotlin.math.ceil
import kotlin.math.floor

class PlayerDataInventory : InventoryDefinition {
    override val id: String = "playerDataInventory"

    override val inventory = buildChestInterface {

        titleSupplier = { Component.text("Player data explorer") }
        rows = 4

        var _pageProperty = interfaceProperty(1)
        var page by _pageProperty

        withTransform(_pageProperty) { pane, view ->
            val playerCount = Bukkit.getOnlinePlayers().size
            val maxPages = maxOf(1, ceil(playerCount / 30.0).toInt())
            val players = Bukkit.getOnlinePlayers()

            players.forEachIndexed { index, player ->
                if (index < (page - 1) * 30 || index >= (page * 30)) return@forEachIndexed

                val data = DataManager.playerData[player]
                if(data == null) {
                    TreeTumblers.LOGGER.warning("⚠️ | Player ${player.name} does not have player data")
                    return@forEachIndexed
                }

                val team: DataManager.TeamData? = DataManager.teamData.values.find { teamData ->
                    teamData.players.contains(player)
                }
                val teamID = team?.id

                val x = floor((index % 8).toDouble()).toInt()
                val y = floor((index / 8).toDouble()).toInt()

                val item = ItemStack(Material.PLAYER_HEAD)
                item.name(
                    Component.empty()
                        .append(Component.translatable("%nox_uuid%${player.uniqueId},false,0,0,1.0","").color(NamedTextColor.WHITE))
                        .append(Component.text(" ${player.name}").color(NamedTextColor.GOLD))
                        .decoration(TextDecoration.ITALIC, false)
                )

                item.description(listOf<Component>(
                        Component.empty()
                            .append(Format.formatTeamName(teamID))
                            .decoration(TextDecoration.ITALIC, false),
                        Component.empty()
                            .append(Component.text("Score: ").color(NamedTextColor.YELLOW))
                            .append(Component.text(team?.score ?: 0).color(NamedTextColor.WHITE))
                            .decoration(TextDecoration.ITALIC, false)
                    )
                )

                val itemMeta = item.itemMeta!! as SkullMeta
                itemMeta.owningPlayer = player
                item.itemMeta = itemMeta

                pane[x, y] = StaticElement(Drawable.drawable(item))
            }

            if(page > 1) {
                val previous = ItemStack(Material.ARROW)
                previous.name(Component.text("Previous page").color(NamedTextColor.YELLOW))
                previous.description(Component.text("Currently page ").append(Component.text(page.toString())).color(NamedTextColor.GRAY))

                pane[3,0] = StaticElement(Drawable.drawable(previous)) {
                    page -= 1
                }
            }

            if(page < maxPages) {
                val next = ItemStack(Material.ARROW)
                next.name(Component.text("Next page").color(NamedTextColor.YELLOW))
                next.description(Component.text("Currently page ").append(Component.text(page.toString())).color(NamedTextColor.GRAY))

                pane[3,8] = StaticElement(Drawable.drawable(next)) {
                    page += 1
                }
            }
        }

    }
}
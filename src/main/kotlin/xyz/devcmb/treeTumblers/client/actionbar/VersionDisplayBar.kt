package xyz.devcmb.treeTumblers.client.actionbar

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import xyz.devcmb.treeTumblers.Constants

class VersionDisplayBar(val player: Player) : IActionBar {
    override val name: String = "versionDisplay"

    override fun send() {
        var actionBar: Component =
            Component.empty()
                .append(Component.text("Tree Tumblers ").color(NamedTextColor.GREEN))
                .append(Component.text("|").color(NamedTextColor.WHITE))
                .append(Component.text(" v" + Constants.VERSION).color(NamedTextColor.GRAY))

        if (Constants.DEV_MODE) {
            actionBar = actionBar.append(Component.text(" (indev)").color(NamedTextColor.GOLD))
        }

        player.sendActionBar(actionBar)
    }
}
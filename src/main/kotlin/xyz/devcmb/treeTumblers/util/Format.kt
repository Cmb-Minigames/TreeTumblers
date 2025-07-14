package xyz.devcmb.treeTumblers.util

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.OfflinePlayer
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.data.DataManager
import java.util.Map

object Format {
    fun format(string: String, type: FormatType): Component {
        val data = type.data
        return Component.text(
            data["prefix"].toString()
        ).append(
            Component.text(" ")
                .append(Component.text(string).color(data["color"] as NamedTextColor?))
        )
    }

    fun format(component: Component, type: FormatType): Component {
        val data = type.data
        return Component.text(
            data["prefix"].toString()
        ).append(
            Component.text(" ")
                .append(component)
        )
    }

    fun stringToUnicode(string: String): String {
        return string.replace("\\u", "").toInt(16).toChar().toString()
    }

    fun formatPlayerName(player: OfflinePlayer): Component {

        return Component.empty()
    }

    fun formatTeamName(team: String?): Component {
        if(team == null) {
            // TODO: add spectator icon
            return Component.empty()
                .append(Component.text("Spectator").color(NamedTextColor.DARK_GRAY))
        }

        val data = DataManager.teamData[team]
        if(data == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Can not format a team name for a nonexistent team ($team)")
            return Component.empty()
        }

        return Component.empty()
            .append(Component.text(stringToUnicode(data.icon)).font(Key.key("tumbling:icons")).color(NamedTextColor.WHITE))
            .append(Component.text(" ${data.displayName}").color(TextColor.fromHexString(data.color)))
    }

    enum class FormatType(val data: MutableMap<String?, Any?>) {
        SUCCESS(
            Map.of<String?, Any?>(
                "prefix", "✅",
                "color", NamedTextColor.GREEN
            )
        ),
        WARNING(
            Map.of<String?, Any?>(
                "prefix", "⚠️",
                "color", NamedTextColor.YELLOW
            )
        ),
        ERROR(
            Map.of<String?, Any?>(
                "prefix", "❌",
                "color", NamedTextColor.DARK_RED
            )
        ),
        INVALID(
            Map.of<String?, Any?>(
                "prefix", "❓",
                "color", NamedTextColor.RED
            )
        ),
        INFO(
            Map.of<String?, Any?>(
                "prefix", "ℹ️",
                "color", NamedTextColor.AQUA
            )
        )
    }
}
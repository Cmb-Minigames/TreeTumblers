package xyz.devcmb.treeTumblers.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.OfflinePlayer
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
        // TODO
        return Component.empty()
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
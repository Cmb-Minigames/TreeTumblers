package xyz.devcmb.treeTumblers.util;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Map;

public class Format {
    public static Component format(String string, FormatType type) {
        Map<String, Object> data = type.getData();
        return Component.text(
                data.get("prefix").toString()
        ).append(Component.text(" ")
        .append(Component.text(string).color((NamedTextColor)data.get("color"))));
    }

    public enum FormatType {
        SUCCESS(Map.of(
            "prefix", "✅",
            "color", NamedTextColor.GREEN
        )),
        WARNING(Map.of(
                "prefix", "⚠️",
                "color", NamedTextColor.YELLOW
        )),
        ERROR(Map.of(
                "prefix", "❌",
                "color", NamedTextColor.DARK_RED
        )),
        INVALID(Map.of(
                "prefix", "❓",
                "color", NamedTextColor.RED
        )),
        INFO(Map.of(
                "prefix", "ℹ️",
                "color", NamedTextColor.AQUA
        ));

        private final Map<String, Object> data;
        FormatType(Map<String, Object> map) {
            data = map;
        }

        public Map<String, Object> getData() {
            return data;
        }
    }
}

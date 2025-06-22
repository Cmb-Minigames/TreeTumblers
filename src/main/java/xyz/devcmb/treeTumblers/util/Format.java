package xyz.devcmb.treeTumblers.util;


import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.devcmb.treeTumblers.data.DataManager;

import java.util.Map;

public class Format {
    public static Component format(String string, FormatType type) {
        Map<String, Object> data = type.getData();
        return Component.text(
                data.get("prefix").toString()
        ).append(Component.text(" ")
        .append(Component.text(string).color((NamedTextColor)data.get("color"))));
    }

    public static Component format(Component component, FormatType type) {
        Map<String, Object> data = type.getData();
        return Component.text(
                data.get("prefix").toString()
        ).append(Component.text(" ")
                .append(component));
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

    public static String stringToUnicode(String string){
        return Character.toString((char) Integer.parseInt(string.replace("\\u", ""), 16));
    }

    public static Component formatPlayerName(@NotNull OfflinePlayer player){
        if(player.getName() == null) return Component.empty();
        Component formatted;

        DataManager.TeamData teamData = DataManager.GetPlayerTeam(player);
        if(teamData == null){
            formatted = Component.empty().append(
                    Component.text(player.getName()).color(NamedTextColor.GRAY)
            );

            return formatted;
        }

        String iconChar = Character.toString((char) Integer.parseInt(teamData.icon.replace("\\u", ""), 16));
        formatted = Component.empty().append(Component.empty().append(Component.text(iconChar + " ").color(NamedTextColor.WHITE)).font(Key.key("tumbling:icons"))).append(Component.text(player.getName()).color(TextColor.fromHexString(teamData.color)));

        return formatted;
    }
}

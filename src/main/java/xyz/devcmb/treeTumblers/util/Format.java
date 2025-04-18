package xyz.devcmb.treeTumblers.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.devcmb.treeTumblers.data.DataManager;

public class Format {
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

        String iconChar = Character.toString((char) Integer.parseInt(teamData.getIcon().replace("\\u", ""), 16));
        formatted = Component.empty().append(
            Component.text(iconChar + " ")
                    .append(Component.text(player.getName()).color(TextColor.fromHexString(teamData.getColor())))
        );

        return formatted;
    }

    public static Component success(String message){
        return Component.text("✅ ")
                .append(Component.text(message)
                .color(NamedTextColor.GREEN));
    }

    public static Component success(Component message){
        return Component.text("✅ ")
                .append(message);
    }

    public static Component error(String message){
        return Component.text("❓ ")
                .append(Component.text(message)
                .color(NamedTextColor.RED));
    }

    public static Component error(Component message){
        return Component.text("❓ ")
                .append(message);
    }

    public static String stringToUnicode(String string){
        return Character.toString((char) Integer.parseInt(string.replace("\\u", ""), 16));
    }
}

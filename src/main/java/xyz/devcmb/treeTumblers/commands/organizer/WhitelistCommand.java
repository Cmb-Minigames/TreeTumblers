package xyz.devcmb.treeTumblers.commands.organizer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WhitelistCommand implements CommandExecutor {
    private static final HttpClient client = HttpClient.newHttpClient();
    private final List<String> WhitelistOptions = List.of(
            "add", "remove", "list"
    );

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(args.length != 3){
            commandSender.sendMessage(
                    Component.text("❓ ")
                        .append(Component.text("Usage: /whitelist <add|remove|list> <player> <team>")
                        .color(NamedTextColor.RED))
            );
            return true;
        }

        if(!WhitelistOptions.contains(args[0].toLowerCase())){
            commandSender.sendMessage(
                    Component.text("❓ ")
                        .append(Component.text("Invalid option. Use add, remove, or list.")
                        .color(NamedTextColor.RED))
            );
            return true;
        }

        if(!isValidMinecraftPlayer(args[1])){
            commandSender.sendMessage(
                    Component.text("❓ ")
                        .append(Component.text("Player not found.")
                        .color(NamedTextColor.RED))
            );
            return true;
        }

       // TODO: Finish

        return true;
    }

    public static boolean isValidMinecraftPlayer(String username) {
        String mojangAPI = "https://api.mojang.com/users/profiles/minecraft/" + username;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(mojangAPI))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                return json.has("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

package xyz.devcmb.treeTumblers.commands.admin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.util.Database;
import xyz.devcmb.treeTumblers.util.Format;

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
        if(args.length == 0){
            commandSender.sendMessage(
                    Format.format("Usage: /whitelist <add | remove | list> <player> <team>", Format.FormatType.INVALID)
            );
            return true;
        }

        if(!WhitelistOptions.contains(args[0].toLowerCase())){
            commandSender.sendMessage(
                    Format.format("Invalid option. Use add, remove, or list.", Format.FormatType.INVALID)
            );
            return true;
        }

        if(args.length >= 2 && !isValidMinecraftPlayer(args[1])){
            commandSender.sendMessage(
                    Format.format("Player not found.", Format.FormatType.INVALID)
            );
            return true;
        }

        switch (args[0]) {
            case "add" -> {
                if (args.length != 3) {
                    commandSender.sendMessage(
                            Format.format("Usage: /whitelist add <player> <team>", Format.FormatType.INVALID)
                    );
                    return true;
                }

                DataManager.TeamData teamData = DataManager.teamData.get(args[2].toLowerCase());
                if (!args[2].equals("spectator") && teamData == null) {
                    commandSender.sendMessage(
                            Format.format("Team not found.", Format.FormatType.INVALID)
                    );
                    return true;
                }

                String playerName = args[1];
                String id = args[2];
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);

                if (player.isWhitelisted()) {
                    commandSender.sendMessage(
                            Format.format("Player is already whitelisted", Format.FormatType.INVALID)
                    );
                    return true;
                }

                if(id.equals("spectator")){
                    id = null;
                }

                Boolean success = Database.setTeam(player, id, playerName);
                if (success) {
                    player.setWhitelisted(true);
                    DataManager.PlayerWhitelist(player, id);

                    commandSender.sendMessage(
                            Format.format(Component.text("Player ")
                                    .color(NamedTextColor.GREEN)
                                    .append(Format.formatPlayerName(player))
                                    .append(Component.text(" has been added to the whitelist.").color(NamedTextColor.GREEN)), Format.FormatType.SUCCESS)
                    );
                } else {
                    commandSender.sendMessage(
                            Format.format("Failed to add player to the team.", Format.FormatType.INVALID)
                    );
                }
            }
            case "remove" -> {
                if (args.length != 2) {
                    commandSender.sendMessage(
                            Format.format("Usage: /whitelist remove <player>", Format.FormatType.INVALID)
                    );
                    return true;
                }

                String playerName = args[1];
                OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
                if (!player.isWhitelisted()) {
                    commandSender.sendMessage(
                            Format.format("Player is not whitelisted.", Format.FormatType.INVALID)
                    );
                    return true;
                }

                Boolean success = Database.removePlayer(player);
                if (success) {
                    player.setWhitelisted(false);
                    DataManager.PlayerRemoveWhitelist(player);
                    commandSender.sendMessage(
                            Format.format(Component.text("Player ")
                                            .color(NamedTextColor.GREEN)
                                    .append(Format.formatPlayerName(player))
                                    .append(Component.text(" has been removed from the whitelist.").color(NamedTextColor.GREEN)), Format.FormatType.SUCCESS)
                    );
                } else {
                    commandSender.sendMessage(
                            Format.format("Failed to remove player from the whitelist.", Format.FormatType.ERROR)
                    );
                }
            }
            case "list" -> {
                List<OfflinePlayer> whitelistedPlayers = Bukkit.getWhitelistedPlayers().stream().toList();
                if (whitelistedPlayers.isEmpty()) {
                    commandSender.sendMessage(
                            Format.format("No players are currently whitelisted.", Format.FormatType.INVALID)
                    );
                } else {
                    final Component[] component = {Component.text("✅ ")
                            .append(Component.text("Whitelisted Players:")
                            .color(NamedTextColor.GREEN))};

                    Bukkit.getWhitelistedPlayers().forEach(player -> {
                        if (player == null) return;

                        component[0] = component[0].append(
                                Component.newline()
                        ).append(Component.text("• ").append(Format.formatPlayerName(player)));
                    });

                    commandSender.sendMessage(
                            component[0]
                    );
                }
            }
            default -> {
                commandSender.sendMessage(
                        Format.format("Invalid option. Use add, remove, or list.", Format.FormatType.INVALID)
                );
                return true;
            }
        }

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
            TreeTumblers.LOGGER.info("Error checking player: " + e.getMessage());
        }
        return false;
    }
}

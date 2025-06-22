package xyz.devcmb.treeTumblers.commands.completions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devcmb.treeTumblers.data.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WhitelistCommandCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return List.of("add", "remove", "list");
        } else if(args.length == 2){
            if(args[0].equals("remove")){
                return Bukkit.getWhitelistedPlayers().stream()
                        .map(OfflinePlayer::getName)
                        .collect(Collectors.toList());
            }
        } else if (args.length == 3) {
            if (args[0].equals("add")) {
                List<String> teams = new ArrayList<>(DataManager.teamData.keySet());
                teams.add("spectator");
                return teams;
            }
        }

        return List.of();
    }
}
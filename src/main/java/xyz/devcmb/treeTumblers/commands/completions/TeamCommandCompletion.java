package xyz.devcmb.treeTumblers.commands.completions;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.devcmb.treeTumblers.data.DataManager;

import java.util.List;

public class TeamCommandCompletion implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 1){
            return Bukkit.getWhitelistedPlayers().stream().map(OfflinePlayer::getName).toList();
        } else if (args.length == 2){
            return DataManager.teamData.keySet().stream().toList();
        }

        return List.of();
    }
}

package xyz.devcmb.treeTumblers.commands.organizer;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.util.Format;

public class TeamCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length != 2){
            sender.sendMessage(Format.error("Usage: /team <player> <team>"));
            return true;
        }

        String playerName = args[0];
        String teamName = args[1];

        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
        if(!player.isWhitelisted()){
            sender.sendMessage(Format.error("Player is not whitelisted."));
        }

        if(teamName.equalsIgnoreCase("spectator")){
            DataManager.setPlayerTeam(player, "spectator");
            return true;
        }

        DataManager.TeamData team = DataManager.teamData.get(teamName.toLowerCase());
        if(team == null){
            sender.sendMessage(Format.error("Team not found."));
            return true;
        }

        DataManager.setPlayerTeam(player, teamName.toLowerCase());
        sender.sendMessage(
                Format.success(
                        Component.empty().append(Component.text("Player ").color(NamedTextColor.GREEN))
                                .append(Format.formatPlayerName(player))
                                .append(Component.text(" has been added to team ")
                                        .color(NamedTextColor.GREEN)
                                        .append(Component.text(teamName).color(TextColor.fromHexString(team.getColor())))
                                )
                )
        );

        return true;
    }
}

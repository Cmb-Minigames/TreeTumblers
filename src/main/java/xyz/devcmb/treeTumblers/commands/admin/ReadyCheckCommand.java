package xyz.devcmb.treeTumblers.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.devcmb.treeTumblers.game.GameManager;
import xyz.devcmb.treeTumblers.util.Format;

public class ReadyCheckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        GameManager.sendReadyCheck();
        commandSender.sendMessage(Format.format("Sent ready check!", Format.FormatType.SUCCESS));
        return true;
    }
}

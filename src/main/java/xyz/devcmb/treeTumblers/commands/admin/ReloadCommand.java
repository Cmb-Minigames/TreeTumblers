package xyz.devcmb.treeTumblers.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.devcmb.treeTumblers.Constants;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.util.Format;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if(!Constants.DEV_MODE){
            commandSender.sendMessage(Format.format("Dev mode must be enabled in order to reload database info.", Format.FormatType.INVALID));
            return true;
        }

        DataManager.reload();
        commandSender.sendMessage(Format.format("Reloaded!", Format.FormatType.SUCCESS));
        return true;
    }
}

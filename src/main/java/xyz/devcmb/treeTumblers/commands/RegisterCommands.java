package xyz.devcmb.treeTumblers.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.commands.admin.ReloadCommand;

import java.util.Objects;

public class RegisterCommands {
    public static void Register(){
        registerSingleCommand("reload", new ReloadCommand());
    }

    /**
     * Registers a single command
     * @param command The command name
     * @param executor The command executor class
     */
    public static void registerSingleCommand(String command, CommandExecutor executor){
        TreeTumblers plugin = TreeTumblers.getPlugin();
        Objects.requireNonNull(plugin.getCommand(command)).setExecutor(executor);
    }

    /**
     * Registers a single tab completion
     * @param command The command name
     * @param completer The tab completer class
     */
    public static void registerSingleTabCompletion(String command, TabCompleter completer){
        TreeTumblers plugin = TreeTumblers.getPlugin();
        Objects.requireNonNull(plugin.getCommand(command)).setTabCompleter(completer);
    }
}

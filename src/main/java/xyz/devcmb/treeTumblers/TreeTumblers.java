package xyz.devcmb.treeTumblers;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.devcmb.treeTumblers.commands.RegisterCommands;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;
import xyz.devcmb.treeTumblers.util.Database;

import java.util.logging.Logger;

public final class TreeTumblers extends JavaPlugin {
    public static Logger LOGGER;
    private static TreeTumblers plugin;

    public static TreeTumblers getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        plugin = this;

        saveDefaultConfig();

        Database.connect();
        DataManager.registerAllTeams();
        ListenerManager.registerAllListeners();
        RegisterCommands.Register();
    }

    @Override
    public void onDisable() {
        Database.disconnect();
    }
}

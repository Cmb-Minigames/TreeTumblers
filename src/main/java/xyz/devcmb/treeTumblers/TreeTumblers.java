package xyz.devcmb.treeTumblers;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devcmb.treeTumblers.commands.RegisterCommands;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;
import xyz.devcmb.treeTumblers.interfaces.UIManager;
import xyz.devcmb.treeTumblers.util.Database;

import java.util.logging.Logger;

public final class TreeTumblers extends JavaPlugin {
    private static TreeTumblers plugin;
    public static Logger LOGGER;

    public static TreeTumblers getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = getLogger();

        saveDefaultConfig();

        Database.connect();
        ListenerManager.RegisterListeners();
        RegisterCommands.Register();
        UIManager.RegisterActionBars();
    }

    @Override
    public void onDisable() {
        Database.disconnect();
    }
}

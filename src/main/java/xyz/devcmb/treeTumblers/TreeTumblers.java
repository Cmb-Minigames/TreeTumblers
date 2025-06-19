package xyz.devcmb.treeTumblers;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;

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

        ListenerManager.registerAllListeners();
    }

    @Override
    public void onDisable() {
    }
}

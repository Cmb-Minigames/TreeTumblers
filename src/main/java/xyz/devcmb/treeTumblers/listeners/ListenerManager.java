package xyz.devcmb.treeTumblers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import xyz.devcmb.treeTumblers.TreeTumblers;

public class ListenerManager {
    public static void RegisterListeners(){
        TreeTumblers plugin = TreeTumblers.getPlugin();
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new PlayerConnections(), plugin);
    }
}

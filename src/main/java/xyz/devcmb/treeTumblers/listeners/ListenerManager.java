package xyz.devcmb.treeTumblers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import xyz.devcmb.treeTumblers.TreeTumblers;

public class ListenerManager {
    public static void registerAllListeners(){
        registerListener(new PlayerAdded());
    }

    public static void registerListener(Listener listener){
        TreeTumblers plugin = TreeTumblers.getPlugin();
        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(listener, plugin);
    }
}

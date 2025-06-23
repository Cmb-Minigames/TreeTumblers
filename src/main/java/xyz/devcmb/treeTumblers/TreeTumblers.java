package xyz.devcmb.treeTumblers;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devcmb.treeTumblers.commands.RegisterCommands;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;
import xyz.devcmb.treeTumblers.packets.PacketManager;
import xyz.devcmb.treeTumblers.util.Database;

import java.util.logging.Logger;

public final class TreeTumblers extends JavaPlugin {
    public static Logger LOGGER;
    private static TreeTumblers plugin;
    private PacketManager packetManager;

    public static TreeTumblers getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        plugin = this;

        saveDefaultConfig();
        PacketEvents.getAPI().init();
        PacketManager.registerAllPacketListeners();

        Database.connect();
        DataManager.registerAllTeams();
        ListenerManager.registerAllListeners();
        RegisterCommands.Register();
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
        Database.disconnect();
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }
}

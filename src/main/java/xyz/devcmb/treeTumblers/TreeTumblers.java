package xyz.devcmb.treeTumblers;
import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devcmb.treeTumblers.commands.RegisterCommands;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;
import xyz.devcmb.treeTumblers.interfaces.UIManager;
import xyz.devcmb.treeTumblers.packets.PacketManager;
import xyz.devcmb.treeTumblers.timers.TimerManager;
import xyz.devcmb.treeTumblers.util.Database;

import java.util.logging.Logger;

public final class TreeTumblers extends JavaPlugin {
    private static TreeTumblers plugin;
    public static Logger LOGGER;
    public static TreeTumblers getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad(){
        // Packets
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = getLogger();

        saveDefaultConfig();

        // Packets
        PacketEvents.getAPI().init();
        PacketManager.RegisterPacketManipulators();

        // Player stuff
        Database.connect();
        DataManager.RegisterTeams();
        ListenerManager.RegisterListeners();

        // Misc
        TimerManager.registerAllTimers();
        RegisterCommands.Register();

        // UI
        UIManager.RegisterActionBars();
        UIManager.RegisterScoreboards();
    }

    @Override
    public void onDisable() {
        Database.disconnect();
        PacketEvents.getAPI().terminate();
    }
}

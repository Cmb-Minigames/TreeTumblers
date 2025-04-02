package xyz.devcmb.treeTumblers;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.devcmb.treeTumblers.commands.RegisterCommands;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;
import xyz.devcmb.treeTumblers.interfaces.UIManager;
import xyz.devcmb.treeTumblers.packets.PacketManager;
import xyz.devcmb.treeTumblers.util.Database;

import java.util.logging.Logger;

public final class TreeTumblers extends JavaPlugin {
    private static TreeTumblers plugin;
    public static Logger LOGGER;
    private static ProtocolManager protocolManager;

    public static TreeTumblers getPlugin() {
        return plugin;
    }
    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    @Override
    public void onEnable() {
        plugin = this;
        LOGGER = getLogger();
        protocolManager = ProtocolLibrary.getProtocolManager();

        saveDefaultConfig();

        Database.connect();
        PacketManager.RegisterPacketManipulators();
        DataManager.RegisterTeams();
        ListenerManager.RegisterListeners();
        RegisterCommands.Register();
        UIManager.RegisterActionBars();
    }

    @Override
    public void onDisable() {
        Database.disconnect();
    }
}

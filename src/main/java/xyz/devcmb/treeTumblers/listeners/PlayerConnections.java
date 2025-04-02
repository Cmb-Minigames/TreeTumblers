package xyz.devcmb.treeTumblers.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.interfaces.PlayerUIDelegate;
import xyz.devcmb.treeTumblers.interfaces.UIManager;
import xyz.devcmb.treeTumblers.util.Database;

public class PlayerConnections implements Listener {
    @EventHandler
    public void onPlayerAdded(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Database.newPlayer(player);
        PlayerUIDelegate delegate = UIManager.PlayerJoin(player);
        delegate.showActionBar("VersionDisplay", false);
        DataManager.RegisterPlayer(player);

        Bukkit.getOnlinePlayers().forEach(plr -> {
           player.unlistPlayer(plr);
           plr.unlistPlayer(player);
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UIManager.PlayerLeave(player);
    }
}

package xyz.devcmb.treeTumblers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.devcmb.treeTumblers.interfaces.PlayerUIDelegate;
import xyz.devcmb.treeTumblers.interfaces.UIManager;

public class PlayerConnections implements Listener {
    @EventHandler
    public void onPlayerAdded(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerUIDelegate delegate = UIManager.PlayerJoin(player);
        delegate.showActionBar("VersionDisplay", false);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        UIManager.PlayerLeave(player);
    }
}

package xyz.devcmb.treeTumblers.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.devcmb.treeTumblers.Constants;
import xyz.devcmb.treeTumblers.ui.PlayerUIController;
import xyz.devcmb.treeTumblers.ui.UIManager;

public class PlayerAdded implements Listener {
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerUIController controller = UIManager.playerAdded(player);

        if(Constants.DEV_MODE) {
            controller.setActionBar("version");
        }
    }
}

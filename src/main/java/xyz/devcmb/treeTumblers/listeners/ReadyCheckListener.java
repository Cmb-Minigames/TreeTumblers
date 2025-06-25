package xyz.devcmb.treeTumblers.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import xyz.devcmb.treeTumblers.Constants;
import xyz.devcmb.treeTumblers.game.GameManager;

public class ReadyCheckListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player) event.getPlayer();
        Component title = event.getView().title();
        if(
                title.equals(Constants.READY_CHECK_TITLE)
                && GameManager.isReadyCheckPending()
                && GameManager.getReadyCheckPendingPlayers().contains(player)
        ){
            GameManager.playerMarkUnready(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Component title = event.getView().title();

        if(title == Constants.READY_CHECK_TITLE && GameManager.getReadyCheckPendingPlayers().contains(player)){
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem == null) return;

            if(currentItem.getType() == Constants.READY_CHECK_ACCEPT_ITEM) {
                GameManager.playerMarkReady(player);
                player.closeInventory();
            } else if (currentItem.getType() == Constants.READY_CHECK_DENY_ITEM) {
                GameManager.playerMarkUnready(player);
                player.closeInventory();
            }
        }
    }
}

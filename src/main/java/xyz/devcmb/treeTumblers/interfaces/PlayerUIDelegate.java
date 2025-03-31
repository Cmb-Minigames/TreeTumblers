package xyz.devcmb.treeTumblers.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.interfaces.actionBars.ActionBar;

public class PlayerUIDelegate {
    private ActionBar activeActionBar;
    private final BukkitTask actionBarSender;
    private final Player player;

    public PlayerUIDelegate(Player player){
        this.player = player;

        actionBarSender = Bukkit.getScheduler().runTaskTimer(TreeTumblers.getPlugin(), () -> {
            if(activeActionBar == null) return;
            activeActionBar.SendActionBar(player);
        }, 0, 20);
    }

    public void showActionBar(String actionBarName, Boolean override){
        ActionBar actionBar = UIManager.actionBars.get(actionBarName);
        if(actionBar == null){
            TreeTumblers.LOGGER.warning("Attempted to show an ActionBar that does not exist: " + actionBarName);
            return;
        }

        if(
            activeActionBar != null
            && activeActionBar.GetPriority().value > actionBar.GetPriority().value
            && !override
        ){
            TreeTumblers.LOGGER.warning("Attempted to show an ActionBar with a lower priority than the currently active one. Ignoring.");
            return;
        }

        activeActionBar = actionBar;
        actionBar.SendActionBar(player);
    }

    public void disconnectPlayerTask(){
        actionBarSender.cancel();
    }
}

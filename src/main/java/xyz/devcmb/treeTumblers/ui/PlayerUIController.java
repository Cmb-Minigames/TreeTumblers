package xyz.devcmb.treeTumblers.ui;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.ui.actionbars.Actionbar;
import xyz.devcmb.treeTumblers.ui.actionbars.VersionDisplay;

import java.util.HashMap;
import java.util.Map;

public class PlayerUIController {
    public final Player player;
    public static Map<String, Actionbar> actionbars = new HashMap<>();
    private BukkitRunnable actionBarRunnable = null;

    public PlayerUIController(Player player) {
        this.player = player;
    }

    public void registerAllActionBars(){
        registerActionbar(new VersionDisplay(this.player));
    }

    private void registerActionbar(Actionbar actionbar){
       actionbars.put(actionbar.getID(), actionbar);
    }

    public void setActionBar(String id) {
        Actionbar actionbar = actionbars.get(id);
        if(actionbar == null) return;

        if(actionBarRunnable != null) actionBarRunnable.cancel();
        actionBarRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                actionbar.send();
            }
        };
        actionBarRunnable.runTaskTimer(TreeTumblers.getPlugin(), 0, 20);
    }
}

package xyz.devcmb.treeTumblers.ui;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.ui.actionbars.Actionbar;
import xyz.devcmb.treeTumblers.ui.actionbars.VersionDisplay;
import xyz.devcmb.treeTumblers.ui.scoreboards.IScoreboard;
import xyz.devcmb.treeTumblers.ui.scoreboards.TeamScoreboard;

import java.util.HashMap;
import java.util.Map;

public class PlayerUIController {
    public final Player player;
    public static Map<String, Actionbar> actionbars = new HashMap<>();
    public static Map<String, IScoreboard> scoreboards = new HashMap<>();
    private BukkitRunnable actionBarRunnable = null;

    public PlayerUIController(Player player) {
        this.player = player;

        registerAllActionBars();
        registerAllScoreboards();
    }

    public void registerAllActionBars(){
        registerActionbar(new VersionDisplay(this.player));
    }
    private void registerActionbar(Actionbar actionbar){
       actionbars.put(actionbar.getID(), actionbar);
    }
    public void registerAllScoreboards() {
        registerScoreboard(new TeamScoreboard(this.player));
    }
    private void registerScoreboard(IScoreboard scoreboard){
        scoreboards.put(scoreboard.getID(), scoreboard);
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

    public void setScoreboard(String id) {
        IScoreboard scoreboard = scoreboards.get(id);
        if(scoreboard == null) return;

        this.player.setScoreboard(scoreboard.getScoreboard());
    }
}

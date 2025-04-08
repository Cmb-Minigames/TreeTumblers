package xyz.devcmb.treeTumblers.interfaces;

import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.interfaces.actionBars.ActionBar;
import xyz.devcmb.treeTumblers.interfaces.actionBars.VersionDisplay;
import xyz.devcmb.treeTumblers.interfaces.scoreboards.sidebars.SidebarScoreboard;
import xyz.devcmb.treeTumblers.interfaces.scoreboards.sidebars.TeamScores;

import java.util.HashMap;
import java.util.Map;

public class UIManager {
    public static Map<String, ActionBar> actionBars = new HashMap<>();
    public static Map<String, SidebarScoreboard> sidebars = new HashMap<>();
    public static Map<Player, PlayerUIDelegate> delegates = new HashMap<>();

    public static PlayerUIDelegate PlayerJoin(Player player){
        PlayerUIDelegate delegate = new PlayerUIDelegate(player);
        delegates.put(player, delegate);
        return delegate;
    }

    public static void PlayerLeave(Player player){
        if(delegates.containsKey(player)){
            delegates.get(player).disconnectPlayerTask();
            delegates.remove(player);
        }
    }

    private static void registerActionBar(ActionBar actionBar){
        if(actionBar == null) {
            TreeTumblers.LOGGER.warning("Attempted to register a null ActionBar. Ignoring.");
            return;
        }

        String name = actionBar.getName();
        if(actionBars.containsKey(name)){
            TreeTumblers.LOGGER.warning("Attempted to register an ActionBar with a name that already exists: " + name);
            return;
        }

        actionBars.put(name, actionBar);
    }

    private static void registerSidebar(SidebarScoreboard scoreboard){
        if(scoreboard == null){
            TreeTumblers.LOGGER.warning("Attempted to register a null SidebarScoreboard. Ignoring.");
            return;
        }

        String name = scoreboard.getName();
        if(sidebars.containsKey(name)){
            TreeTumblers.LOGGER.warning("Attempted to register a SidebarScoreboard with a name that already exists: " + name);
            return;
        }

        sidebars.put(name, scoreboard);
    }

    public static void RegisterActionBars(){
        registerActionBar(new VersionDisplay());
    }
    public static void RegisterScoreboards(){
        registerSidebar(new TeamScores());
    }
}

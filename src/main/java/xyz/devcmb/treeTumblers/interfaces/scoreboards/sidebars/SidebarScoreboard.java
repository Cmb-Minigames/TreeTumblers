package xyz.devcmb.treeTumblers.interfaces.scoreboards.sidebars;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import xyz.devcmb.treeTumblers.interfaces.UIPriority;

public interface SidebarScoreboard {
    Scoreboard GetScoreboard(Player player);
    UIPriority GetPriority();
    String getName();
}

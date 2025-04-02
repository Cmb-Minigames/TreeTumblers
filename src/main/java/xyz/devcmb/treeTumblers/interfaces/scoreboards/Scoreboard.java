package xyz.devcmb.treeTumblers.interfaces.scoreboards;

import org.bukkit.entity.Player;

public interface Scoreboard {
    void SendScoreboard(Player player);
    Boolean IsDefault();
    String GetName();
}

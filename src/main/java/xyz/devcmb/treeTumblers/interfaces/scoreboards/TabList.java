package xyz.devcmb.treeTumblers.interfaces.scoreboards;

import org.bukkit.entity.Player;

public class TabList implements Scoreboard {
    @Override
    public void SendScoreboard(Player player) {

    }

    @Override
    public Boolean IsDefault() {
        return true;
    }

    @Override
    public String GetName() {
        return "tablist";
    }
}

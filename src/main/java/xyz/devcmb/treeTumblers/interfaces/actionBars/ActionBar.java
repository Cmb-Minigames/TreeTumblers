package xyz.devcmb.treeTumblers.interfaces.actionBars;

import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.interfaces.UIPriority;

public interface ActionBar {
    void SendActionBar(Player player);
    UIPriority GetPriority();
    String getName();
}

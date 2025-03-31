package xyz.devcmb.treeTumblers.interfaces.actionBars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.Constants;
import xyz.devcmb.treeTumblers.interfaces.UIPriority;

public class VersionDisplay implements ActionBar {
    @Override
    public void SendActionBar(Player player){
        Component actionBar =
                Component.empty()
                    .append(Component.text("Tree Tumblers ").color(NamedTextColor.GREEN))
                    .append(Component.text("|").color(NamedTextColor.WHITE))
                    .append(Component.text(" v" + Constants.VERSION).color(NamedTextColor.GRAY));
        if(Constants.DEV_MODE){
            actionBar = actionBar.append(Component.text(" (indev)").color(NamedTextColor.GOLD));
        }

        player.sendActionBar(actionBar);
    }

    @Override
    public UIPriority GetPriority() {
        return UIPriority.LOWEST;
    }

    @Override
    public String getName() {
        return "VersionDisplay";
    }
}

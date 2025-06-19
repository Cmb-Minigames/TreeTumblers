package xyz.devcmb.treeTumblers.ui.actionbars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.Constants;

public class VersionDisplay implements Actionbar {
    private final Player player;
    public VersionDisplay(Player player) {
        this.player = player;
    }

    @Override
    public void send(){
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
    public String getID() {
        return "version";
    }
}

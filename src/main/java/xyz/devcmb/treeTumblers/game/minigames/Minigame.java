package xyz.devcmb.treeTumblers.game.minigames;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.Listener;

public interface Minigame extends Listener {
    String getName();
    TextColor getColor();
    void pregame();
    void start();
    void end();
}

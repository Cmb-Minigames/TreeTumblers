package xyz.devcmb.treeTumblers.ui;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class UIManager {
    public static Map<Player, PlayerUIController> playerUIControllers = new HashMap<>();

    public static PlayerUIController playerAdded(Player player) {
        PlayerUIController playerUIController = new PlayerUIController(player);
        playerUIController.registerAllActionBars();

        playerUIControllers.put(player, playerUIController);
        return playerUIController;
    }
}

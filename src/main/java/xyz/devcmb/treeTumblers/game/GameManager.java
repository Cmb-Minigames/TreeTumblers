package xyz.devcmb.treeTumblers.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.devcmb.treeTumblers.Constants;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.game.minigames.BrawlMinigameController;
import xyz.devcmb.treeTumblers.game.minigames.Minigame;
import xyz.devcmb.treeTumblers.listeners.ListenerManager;
import xyz.devcmb.treeTumblers.util.Format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameManager {
    public static GameState currentGameState = GameState.PRE_EVENT;
    public static String currentMinigame = null;
    private static final Map<String, Minigame> minigames = new HashMap<>();
    private static final List<Minigame> playedMinigames = new ArrayList<>();
    private static boolean readyCheckPending = false;
    private static Boolean readyCheckPassed = null;
    private static final List<Player> readyCheckPendingPlayers = new ArrayList<>();
    private static BukkitRunnable readyCheckCancel = null;

    public static void registerAllMinigames(){
        registerMinigame(new BrawlMinigameController());
    }

    private static void registerMinigame(Minigame minigame){
        minigames.put(minigame.getName(), minigame);
        ListenerManager.registerListener(minigame);
    }

    public static void startEvent() {
        // TODO: Scary!
    }

    public static void sendReadyCheck() {
        AtomicBoolean isGameReady = new AtomicBoolean(true);
        DataManager.teamData.forEach((s, teamData) -> {
            boolean playerOnline = false;
            for (OfflinePlayer plr : teamData.teamMembers) {
                if (plr.isOnline()) playerOnline = true;
            }

            if (!playerOnline && !Constants.DEV_MODE) isGameReady.set(false);
        });

        if (!isGameReady.get()) return;
        readyCheckPendingPlayers.clear();

        DataManager.teamData.forEach((s, teamData) -> {
            List<Player> onlineTeamMembers = new ArrayList<>();
            for (OfflinePlayer plr : teamData.teamMembers) {
                if (plr.isOnline()) onlineTeamMembers.add(plr.getPlayer());
            }

            readyCheckPendingPlayers.addAll(onlineTeamMembers);
        });

        readyCheckPending = true;
        readyCheckCancel = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcast(Format.format("The ready check timed out! Can not proceed!", Format.FormatType.INVALID));
                readyCheckPassed = false;
                readyCheckPendingPlayers.forEach(HumanEntity::closeInventory);
            }
        };

        readyCheckCancel.runTaskLater(TreeTumblers.getPlugin(), 20 * 15);
        readyCheckPendingPlayers.forEach(GameManager::openReadyCheckInventory);
    }

    public static void openReadyCheckInventory(Player player) {
        if(!readyCheckPending) return;

        Inventory inventory = Bukkit.createInventory(player, 27, Constants.READY_CHECK_TITLE);

        ItemStack success = ItemStack.of(Constants.READY_CHECK_ACCEPT_ITEM);
        ItemMeta meta1 = success.getItemMeta();
        meta1.customName(Component.text("Ready!").color(NamedTextColor.GREEN));
        success.setItemMeta(meta1);

        ItemStack fail = ItemStack.of(Constants.READY_CHECK_DENY_ITEM);
        ItemMeta meta2 = fail.getItemMeta();
        meta2.customName(Component.text("Not ready!").color(NamedTextColor.RED));
        fail.setItemMeta(meta2);

        inventory.setItem(12, success);
        inventory.setItem(14, fail);

        player.openInventory(inventory);
    }

    public static void playerMarkReady(Player player) {
        readyCheckPendingPlayers.remove(player);
        if(readyCheckPendingPlayers.isEmpty()) {
            Bukkit.broadcast(Format.format("Ready check succeeded!", Format.FormatType.SUCCESS));

            readyCheckCancel.cancel();
            readyCheckPending = false;
            readyCheckPassed = true;
        }
    }

    public static void playerMarkUnready(Player player) {
        if(!readyCheckPending) return;

        Bukkit.broadcast(Format.format(player.getName() + " failed the ready check!", Format.FormatType.INVALID));
        readyCheckCancel.cancel();
        readyCheckPending = false;
        readyCheckPassed = false;
        readyCheckPendingPlayers.forEach(HumanEntity::closeInventory);
    }

    public static boolean isReadyCheckPending() {
        return readyCheckPending;
    }

    public static List<Player> getReadyCheckPendingPlayers() {
        return readyCheckPendingPlayers;
    }
}

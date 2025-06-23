package xyz.devcmb.treeTumblers.listeners;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import xyz.devcmb.treeTumblers.Constants;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.ui.PlayerUIController;
import xyz.devcmb.treeTumblers.ui.UIManager;
import xyz.devcmb.treeTumblers.util.Database;
import xyz.devcmb.treeTumblers.util.Format;

import java.util.*;

public class PlayerAdded implements Listener {
    Map<Player, BukkitTask> headerTasks = new HashMap<>();

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerUIController controller = UIManager.playerAdded(player);
        controller.setScoreboard("teamscores");

        if(Constants.DEV_MODE) {
            controller.setActionBar("version");
        }

        DataManager.playerJoin(player);

        Bukkit.getOnlinePlayers().forEach(plr -> {
            player.unlistPlayer(plr);
            plr.unlistPlayer(player);
        });

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(TreeTumblers.getPlugin(), () -> {
            Component[] components = {
                    Component.newline()
                            .appendNewline()
                            .append(
                                    Component.text("   \u0001   ")
                                            .font(Key.key("tumbling:icons"))
                            ).appendNewline().appendNewline().appendNewline().appendNewline()
            };

            // Thank you copilot
            // This scares me :D
            Map<String, DataManager.TeamData> sortedTeamData = DataManager.teamData.entrySet().stream()
                    .sorted(Comparator.comparingInt(e -> e.getValue().order))
                    .collect(
                            java.util.stream.Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e3, e4) -> e3,
                                    java.util.LinkedHashMap::new
                            )
                    );

            sortedTeamData.forEach((id, data) -> {
                List<Component> playerComponents = new ArrayList<>();

                data.teamMembers.forEach(plr -> {
                    String name = plr.getName();
                    playerComponents.add(
                            Component.text(
                                    (name == null ? Database.GetDatabasePlayerName(plr.getUniqueId().toString()) : name)
                            ).color(plr.isOnline() ? TextColor.fromHexString(data.color) : NamedTextColor.DARK_GRAY)
                    );
                });

                Component joinedComponents = Component.join(
                        JoinConfiguration.builder().separator(Component.text(" • ").color(NamedTextColor.WHITE)).build(),
                        playerComponents
                );

                components[0] = components[0].append(Component.newline().append(Component.newline()).append(Component.text("    ")).append(
                        Component.empty().append(Component.text(Format.stringToUnicode(data.icon) + " ").font(Key.key("tumbling:icons")))
                                .append(joinedComponents)).append(Component.text("    "))
                );
            });

            components[0] = components[0].appendNewline().appendNewline();

            player.sendPlayerListHeader(components[0]);
        }, 0, 10);

        headerTasks.put(player, task);
    }
}

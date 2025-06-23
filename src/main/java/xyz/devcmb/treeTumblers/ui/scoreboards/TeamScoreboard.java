package xyz.devcmb.treeTumblers.ui.scoreboards;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.timers.Timer;
import xyz.devcmb.treeTumblers.timers.TimerManager;
import xyz.devcmb.treeTumblers.util.Format;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TeamScoreboard implements IScoreboard {
    private final Player plr;

    public TeamScoreboard(Player player) {
        plr = player;
    }

    @Override
    public String getID() {
        return "teamscores";
    }

    @Override
    public Scoreboard getScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(
                "teamScores",
                Criteria.DUMMY,
                Component.empty().append(Component.text("Tree Tumblers").color(NamedTextColor.GREEN))
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score blank1 = objective.getScore(" ");
        blank1.setScore(12);

        Timer activeTimer = TimerManager.activeTimer;
        if (activeTimer != null) {
            Score timer = objective.getScore("timer");
            timer.customName(Component.text("⏱️ ").append(Component.text(activeTimer.getTime()).color(NamedTextColor.YELLOW)));
            timer.setScore(11);

            Score blank2 = objective.getScore("   ");
            blank2.setScore(10);
        }


        Map<String, DataManager.TeamData> teamData = DataManager.teamData;
        teamData = teamData.entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    int pointsComparison = Integer.compare(entry2.getValue().points, entry1.getValue().points);
                    if (pointsComparison != 0) {
                        return pointsComparison;
                    }
                    return Integer.compare(entry1.getValue().order, entry2.getValue().order);
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        AtomicInteger index = new AtomicInteger(9);
        teamData.forEach((id, data) -> {
            TextColor color = TextColor.fromHexString(data.color);
            if(color == null) {
                TreeTumblers.LOGGER.severe("Invalid team color! Could not create team scoreboard objective.");
                return;
            }

            Score teamScore = objective.getScore(id);
            teamScore.customName(Component.empty().append(Component.text(Format.stringToUnicode(data.icon) + " ").font(Key.key("tumbling:icons"))).append(
                    Component.text(data.name).color(color)
            ).append(Component.text(".".repeat(30)).color(NamedTextColor.GRAY)).append(
                    Component.text(data.points).color(color)
            ));

            teamScore.setScore(index.getAndDecrement());
        });

        Score blank3 = objective.getScore("  ");
        blank3.setScore(0);

        return scoreboard;
    }
}
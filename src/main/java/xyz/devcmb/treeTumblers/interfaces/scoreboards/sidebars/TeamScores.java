package xyz.devcmb.treeTumblers.interfaces.scoreboards.sidebars;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import xyz.devcmb.treeTumblers.data.DataManager;
import xyz.devcmb.treeTumblers.interfaces.UIPriority;
import xyz.devcmb.treeTumblers.timers.Timer;
import xyz.devcmb.treeTumblers.timers.TimerManager;
import xyz.devcmb.treeTumblers.util.Format;
import xyz.devcmb.treeTumblers.util.Utilities;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class TeamScores implements SidebarScoreboard {
    @Override
    public Scoreboard GetScoreboard(Player player) {
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
            Score timer = objective.getScore(
                    componentToLegacyString(
                            Component.text("[timer icon] ").append(
                                    Utilities.formatTime(activeTimer.getTime()).color(NamedTextColor.AQUA)
                            )
                    )
            );
            timer.setScore(11);

            Score blank2 = objective.getScore("   ");
            blank2.setScore(10);
        }


        Map<String, DataManager.TeamData> teamData = DataManager.teamData;
        teamData = teamData.entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    int pointsComparison = Integer.compare(entry2.getValue().getPoints(), entry1.getValue().getPoints());
                    if (pointsComparison != 0) {
                        return pointsComparison;
                    }
                    return entry1.getValue().getId().compareTo(entry2.getValue().getId());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        AtomicInteger index = new AtomicInteger(9);
        teamData.forEach((id, data) -> {
            TextColor color = TextColor.fromHexString(data.getColor());
            if(color == null) return;

            Score teamScore = objective.getScore("");
            teamScore.customName(Component.text(Format.stringToUnicode(data.getIcon()) + " ").append(
                    Component.text(data.getName()).color(color)
            ).append(Component.text(".".repeat(30)).color(NamedTextColor.GRAY)).append(
                    Component.text(data.getPoints()).color(color)
            ));

            teamScore.setScore(index.getAndDecrement());
        });

        Score blank3 = objective.getScore("  ");
        blank3.setScore(0);

        return scoreboard;
    }

    @Override
    public UIPriority GetPriority() {
        return UIPriority.LOWEST;
    }

    @Override
    public String getName() {
        return "TeamScores";
    }

    private String componentToLegacyString(Component text){
        return LegacyComponentSerializer.legacySection().serialize(text);
    }
}

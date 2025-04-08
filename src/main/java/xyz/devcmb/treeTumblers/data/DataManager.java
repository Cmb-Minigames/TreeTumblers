package xyz.devcmb.treeTumblers.data;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.util.Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    public static Map<Player, PlayerData> playerData = new HashMap<>();
    public static Map<String, TeamData> teamData = new HashMap<>();

    public static void RegisterPlayer(Player player){
        PlayerData data = new PlayerData(player);
        playerData.put(player, data);
    }

    public static void RegisterTeams(){
        Map<String, Map<String, Object>> teams = Database.getTeams();
        teams.forEach((id, team) -> {
            List<OfflinePlayer> players = Database.getTeamMembers(id);
            TeamData data = new TeamData(team, players);
            teamData.put(id, data);
        });
    }

    public static TeamData GetPlayerTeam(OfflinePlayer player) {
        for (Map.Entry<String, TeamData> entry : teamData.entrySet()) {
            TeamData data = entry.getValue();
            List<OfflinePlayer> players = data.getPlayers();
            for (OfflinePlayer plr : players) {
                if (plr.equals(player)) {
                    return data;
                }
            }
        }

        return null;
    }

    public static void ReloadData(){
        playerData.clear();
        teamData.clear();

        RegisterTeams();
        Bukkit.getOnlinePlayers().forEach(DataManager::RegisterPlayer);
    }

    public static class PlayerData {
        private final Player player;
        private final int points;
        private final String team;

        public PlayerData(Player player){
            this.player = player;

            Map<String, Object> playerData = Database.getPlayer(player);
            if(playerData.isEmpty()){
                this.points = 0;
                this.team = "spectator";
                return;
            }

            this.points = (int) playerData.get("points");
            this.team = (String) playerData.get("team");
        }

        public Player getPlayer() {
            return player;
        }

        public int getPoints() {
            return points;
        }

        public String getTeam() {
            return team;
        }
    }

    public static class TeamData {
        private final String id;
        private final String name;
        private final int points;
        private final String color;
        private final String icon;
        private final List<OfflinePlayer> players;

        public TeamData(Map<String, Object> team, List<OfflinePlayer> players){
            this.id = (String) team.get("id");
            this.name = (String) team.get("name");
            this.points = (int) team.get("points");
            this.color = (String) team.get("color");
            this.icon = (String) team.get("icon");
            this.players = players;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getPoints() {
            return points;
        }

        public String getColor() {
            return color;
        }

        public String getIcon() {
            return icon;
        }

        public List<OfflinePlayer> getPlayers() {
            return players;
        }
    }

}

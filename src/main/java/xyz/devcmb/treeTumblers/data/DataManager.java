package xyz.devcmb.treeTumblers.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.util.Database;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    public static Map<Player, PlayerData> playerData = new HashMap<>();
    public static Map<String, TeamData> teamData = new HashMap<>();

    public static void playerJoin(Player player) {
        playerData.put(player, new PlayerData(player));
    }
    public static void registerAllTeams() {
        Map<String, Map<String, Object>> teams = Database.getAllTeams();
        teams.forEach((id, data) -> teamData.put(id, new TeamData(id)));
    }

    public static class PlayerData {
        public Player player;
        public String team;
        public int points;

        public PlayerData(Player player) {
            this.player = player;
            if(!Database.playerDataExists(player)) Database.newPlayer(player);

            ResultSet data = Database.getPlayerData(player);
            if(data == null) {
                TreeTumblers.LOGGER.severe("Player data for " + player.getName() + " could not be loaded.");
                return;
            }


            try {
                data.next();

                this.team = data.getString("team");
                if(this.team == null) this.team = "spectator";

                this.points = data.getInt("points");
            } catch(Exception err) {
                TreeTumblers.LOGGER.severe("Failed to load user data for player " + player.getName() + ": " + err.getMessage());
            }
        }
    }

    public static class TeamData {
        public String id;
        public String name;
        public String icon;
        public int points;
        public List<OfflinePlayer> teamMembers;

        public TeamData(String id) {
            this.id = id;

            ResultSet data = Database.getTeamData(id);
            if(data == null) {
                TreeTumblers.LOGGER.severe("Team data for " + id + " could not be loaded.");
                return;
            }

            try {
                data.next();

                this.name = data.getString("name");
                this.icon = data.getString("icon");
                this.points = data.getInt("points");
                this.teamMembers = Database.getTeamMembers(id);
            } catch(Exception err) {
                TreeTumblers.LOGGER.severe("Failed to load team data for " + id + ": " + err.getMessage());
            }
        }
    }
}

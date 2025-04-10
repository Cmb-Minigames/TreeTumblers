package xyz.devcmb.treeTumblers.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.TreeTumblers;
import xyz.devcmb.treeTumblers.data.DataManager;

import java.sql.*;
import java.util.*;

/**
 * A utility class for interacting with the MySQL database
 */
public class Database {
    private static Connection connection;

    /**
     * Connect to the MySQL database
     */
    public static void connect() {
        FileConfiguration config = TreeTumblers.getPlugin().getConfig();
        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        String username = config.getString("database.username");
        String password = config.getString("database.password");
        String database = config.getString("database.database");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false";

        try {
            connection = DriverManager.getConnection(url, username, password);
            TreeTumblers.LOGGER.info("Successfully connected to the MySQL database.");
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to connect to the MySQL database: " + e.getMessage());
        }
    }

    /**
     * Disconnect from the MySQL database
     */
    public static void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                TreeTumblers.LOGGER.info("Disconnected from the MySQL database.");
            } catch (SQLException e) {
                TreeTumblers.LOGGER.severe("Failed to disconnect from the MySQL database: " + e.getMessage());
            }
        }
    }

    public static Map<String, Map<String, Object>> getTeams(){
        Map<String, Map<String, Object>> teams = new HashMap<>();

        if(connection == null){
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return teams;
        }

        try {
            String query = "SELECT * FROM Teams";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                Map<String, Object> teamData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    teamData.put(columnName, value);
                }
                teams.put(id, teamData);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to retrieve teams from the database: " + e.getMessage());
        }

        return teams;
    }

    public static Map<String, Object> getPlayer(Player player){
        Map<String, Object> plr = new HashMap<>();

        try {
            String query = "SELECT * FROM Players WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    plr.put(columnName, value);
                }
            } else {
                TreeTumblers.LOGGER.warning("Player with UUID " + player.getUniqueId() + " not found in the database.");
            }
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to retrieve player data: " + e.getMessage());
        }

        return plr;
    }

    public static void newPlayer(Player player) {
        if (connection == null) {
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return;
        }

        try {
            String checkQuery = "SELECT COUNT(*) FROM Players WHERE uuid = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = checkStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            checkStatement.close();

            if (count > 0) {
                return;
            }

            String insertQuery = "INSERT INTO Players (uuid, team, points) VALUES (?, NULL, 0)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, player.getUniqueId().toString());
            insertStatement.executeUpdate();
            insertStatement.close();
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to create new player: " + e.getMessage());
        }
    }

    public static Boolean setTeam(OfflinePlayer player, String id) {
        if (connection == null) {
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return false;
        }

        try {
            String query = "INSERT INTO Players (uuid, team, points) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE team = VALUES(team), points = VALUES(points)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, id);
            statement.setInt(3, 0);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to set team for player: " + e.getMessage());
            return false;
        }
    }

    public static Boolean removePlayer(OfflinePlayer player) {
        if (connection == null) {
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return false;
        }

        try {
            String query = "UPDATE Players SET team = NULL, points = 0 WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getUniqueId().toString());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                TreeTumblers.LOGGER.warning("Player with UUID " + player.getUniqueId() + " not found in the database.");
            }
            statement.close();
            return true;
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to remove player from the database: " + e.getMessage());
            return false;
        }
    }

    public static List<OfflinePlayer> getTeamMembers(String id){
        List<OfflinePlayer> players = new ArrayList<>();

        if (connection == null) {
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return players;
        }

        try {
            String query = "SELECT uuid FROM Players WHERE team = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                players.add(player);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to retrieve team members: " + e.getMessage());
        }

        TreeTumblers.LOGGER.info(players.toString());
        return players;
    }

    public static void updateTeam(DataManager.TeamData data){
        if (connection == null) {
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return;
        }

        try {
            String query = "UPDATE Teams SET points = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, data.getPoints());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to update team data: " + e.getMessage());
        }
    }

    public static void updatePlayer(DataManager.PlayerData data){
        if (connection == null) {
            TreeTumblers.LOGGER.severe("Database connection is not established.");
            return;
        }

        try {
            String query = "UPDATE Players SET points = ?, team = ? WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, data.getPoints());
            statement.setString(2, data.getTeam());
            statement.setString(3, data.getPlayer().getUniqueId().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to update player data: " + e.getMessage());
        }
    }
}

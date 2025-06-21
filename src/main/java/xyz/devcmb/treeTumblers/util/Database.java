package xyz.devcmb.treeTumblers.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.devcmb.treeTumblers.TreeTumblers;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            String insertQuery = "INSERT INTO Players (uuid, name, team, points) VALUES (?, ?, NULL, 0)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, player.getUniqueId().toString());
            insertStatement.setString(2, player.getName());
            insertStatement.executeUpdate();
            insertStatement.close();
        } catch (SQLException e) {
            TreeTumblers.LOGGER.severe("Failed to create new player: " + e.getMessage());
        }
    }

    public static boolean playerDataExists(Player player) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM players WHERE uuid = ? LIMIT 1");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            preparedStatement.close();
            return exists;
        } catch(Exception ex) {
            TreeTumblers.LOGGER.severe("Failed to check if player data exists: " + ex.getMessage());
            return false;
        }
    }

    public static ResultSet getPlayerData(Player player) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            return preparedStatement.executeQuery();
        } catch(Exception ex) {
            TreeTumblers.LOGGER.severe("Failed to get player data: " + ex.getMessage());
        }

        return null;
    }

    public static ResultSet getTeamData(String id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teams WHERE id = ?");
            preparedStatement.setString(1, id);
            return preparedStatement.executeQuery();
        } catch (Exception e) {
            TreeTumblers.LOGGER.severe("Failed to get team data: " + e.getMessage());
        }

        return null;
    }

    public static Map<String, Map<String, Object>> getAllTeams() {
        Map<String, Map<String, Object>> teams = new HashMap<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teams");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                Map<String, Object> teamData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    teamData.put(columnName, resultSet.getObject(i));
                }
                teams.put(id, teamData);
            }
            resultSet.close();
            preparedStatement.close();
        } catch(Exception e) {
            TreeTumblers.LOGGER.severe("Failed to get team data: " + e.getMessage());
        }
        return teams;
    }

    public static List<OfflinePlayer> getTeamMembers(String team) {
        List<OfflinePlayer> players = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE team = ?");
            preparedStatement.setString(1, team);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(resultSet.getString("uuid"));
                players.add(player);
            }
        } catch(Exception e) {
            TreeTumblers.LOGGER.severe("Failed to get team members: " + e.getMessage());
        }

        return players;
    }
}

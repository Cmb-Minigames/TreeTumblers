package xyz.devcmb.treeTumblers.util;

import org.bukkit.configuration.file.FileConfiguration;
import xyz.devcmb.treeTumblers.TreeTumblers;

import java.nio.ByteBuffer;
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

    /**
     * Convert a UUID to a byte array
     * @param uuid The UUID to convert
     * @return The byte array
     */
    private static byte[] uuidToBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }
}

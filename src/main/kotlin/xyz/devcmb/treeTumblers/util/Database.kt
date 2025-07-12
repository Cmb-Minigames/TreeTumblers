package xyz.devcmb.treeTumblers.util

import org.bukkit.configuration.file.FileConfiguration
import xyz.devcmb.treeTumblers.TreeTumblers
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private var connection: Connection? = null

    /**
     * Connect to the MySQL database
     */
    fun connect() {
        val config: FileConfiguration = TreeTumblers.plugin.config
        val host = config.getString("database.host")
        val port = config.getInt("database.port")
        val username = config.getString("database.username")
        val password = config.getString("database.password")
        val database = config.getString("database.database")

        val url = "jdbc:mysql://$host:$port/$database?useSSL=false"

        try {
            connection = DriverManager.getConnection(url, username, password)
            TreeTumblers.LOGGER.info("✅ | Successfully connected to the MySQL database.")
        } catch (e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to connect to the MySQL database: " + e.message)
        }
    }

    /**
     * Disconnect from the MySQL database
     */
    fun disconnect() {
        if (connection != null) {
            try {
                connection!!.close()
                TreeTumblers.LOGGER.info("✅ | Disconnected from the MySQL database successfully.")
            } catch (e: SQLException) {
                TreeTumblers.LOGGER.severe("❌ | Failed to disconnect from the MySQL database: " + e.message)
            }
        }
    }
}
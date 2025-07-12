package xyz.devcmb.treeTumblers.util

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.data.DataManager
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.UUID

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
            TreeTumblers.LOGGER.severe("❌ | Failed to connect to the MySQL database: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
        }
    }

    fun getPlayerDatabaseInformation(player: Player): ResultSet? {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return null
        }

        return try {
            if(userExists(player) == false) {
                createUserData(player, null)
            }

            val statement: PreparedStatement = connection!!.prepareStatement("SELECT * FROM players WHERE uuid = ?")
            statement.setString(1, player.uniqueId.toString())

            val result: ResultSet = statement.executeQuery()
            return if (result.next()) result else null
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to get player db info: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
            null
        }
    }

    fun userExists(player: Player): Boolean? {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return null
        }

        return try {
            val statement: PreparedStatement = connection!!.prepareStatement("SELECT COUNT(*) FROM players WHERE uuid = ?")
            statement.setString(1, player.uniqueId.toString())
            val result: ResultSet = statement.executeQuery()

            result.next() && result.getInt(1) > 0
        } catch (e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to get player db info: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
            null
        }
    }

    fun createUserData(player: Player, team: String?) {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return
        }

        try {
            val statement: PreparedStatement = connection!!.prepareStatement("INSERT INTO players (uuid, name, team, score) VALUES (?, ?, ?, 0)")
            statement.setString(1, player.uniqueId.toString())
            statement.setString(2, player.name)
            statement.setString(3, team)

            statement.executeUpdate()
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to create player db info: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
        }
    }

    fun createUserData(player: OfflinePlayer, name: String, team: String?) {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return
        }

        try {
            val statement: PreparedStatement = connection!!.prepareStatement("INSERT INTO players (uuid, name, team, score) VALUES (?, ?, ?, 0)")
            statement.setString(1, player.uniqueId.toString())
            statement.setString(2, name)
            statement.setString(3, team)

            statement.executeUpdate()
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to create player db info: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
        }
    }

    fun replicatePlayerData(player: Player, data: DataManager.PlayerData) {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return
        }

        try {
            val statement: PreparedStatement = connection!!.prepareStatement("UPDATE players SET team = ?, score = ? WHERE uuid = ?")
            statement.setString(1, data.team)
            statement.setInt(2, data.score)
            statement.setString(3, player.uniqueId.toString())

            statement.executeUpdate()
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to create player db info: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
        }
    }

    fun getTeamIDs(): ResultSet? {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return null
        }

        try {
            val statement: PreparedStatement = connection!!.prepareStatement("SELECT id FROM teams")
            val result: ResultSet = statement.executeQuery()

            return result
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to read teams database: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
            return null
        }
    }

    fun getTeamData(id: String): ResultSet? {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return null
        }

        return try {
            val statement: PreparedStatement = connection!!.prepareStatement("SELECT * FROM teams WHERE id = ?")
            statement.setString(1, id)
            val result: ResultSet = statement.executeQuery()

            return if(result.next()) result else null
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to read teams database: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
            null
        }
    }

    fun getTeamMembers(id: String): MutableList<OfflinePlayer>? {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return null
        }

        return try {
            val statement: PreparedStatement = connection!!.prepareStatement("SELECT uuid FROM players WHERE team = ?")
            statement.setString(1, id)

            val result: ResultSet = statement.executeQuery()
            val players: MutableList<OfflinePlayer> = mutableListOf()

            while(result.next()) {
                val plr: OfflinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(result.getString("uuid")))
                players.add(plr)
            }

            players
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to read teams database: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
            null
        }
    }

    fun replicateTeamData(id: String, data: DataManager.TeamData) {
        if(connection == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Could not query database due to the connection not being established.")
            return
        }

        try {
            val statement: PreparedStatement = connection!!.prepareStatement("UPDATE teams SET score = ? WHERE id = ?")
            statement.setInt(1, data.score)
            statement.setString(2, id)

            statement.executeUpdate()
        } catch(e: SQLException) {
            TreeTumblers.LOGGER.severe("❌ | Failed to create player db info: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
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
                TreeTumblers.LOGGER.severe("❌ | Failed to disconnect from the MySQL database: ${e.message}\n\n${e.stackTrace.joinToString("\n")}")
            }
        }
    }
}
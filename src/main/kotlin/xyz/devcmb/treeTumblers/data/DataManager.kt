package xyz.devcmb.treeTumblers.data

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.devcmb.treeTumblers.Constants
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.util.Database
import java.sql.ResultSet

object DataManager {
    val playerData: MutableMap<Player, PlayerData> = HashMap()
    val teamData: MutableMap<String, TeamData> = HashMap()
    var autosave: BukkitRunnable? = null
    var nextAutosaveTick: Long = 0

    fun getNextAutosaveTime(): String {
        val ticksLeft = (nextAutosaveTick - Bukkit.getServer().currentTick).coerceAtLeast(0)
        val secondsLeft = ticksLeft / 20
        val minutes = secondsLeft / 60
        val seconds = secondsLeft % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    fun getPlayerTeam(player: OfflinePlayer?): TeamData? {
        for (entry in teamData.entries) {
            val data = entry.value
            val players: MutableList<OfflinePlayer> = data.players
            for (plr in players) {
                if (plr == player) {
                    return data
                }
            }
        }

        return null
    }

    fun startup() {
        val teams: ResultSet? = Database.getTeamIDs()
        if (teams == null) {
            TreeTumblers.LOGGER.severe("❌ | Could not get all team data. Aborting load...")
            return
        }

        while (teams.next()) {
            val id: String? = teams.getString("id")
            if (id == null) {
                TreeTumblers.LOGGER.warning("⚠️ | Could not get id field of team data. Skipping...")
                continue
            }

            val data = TeamData(id)
            teamData[id] = data
        }

        autosave = object : BukkitRunnable() {
            override fun run() {
                TreeTumblers.LOGGER.info("ℹ️ | An autosave has started.")
                playerData.forEach(Database::replicatePlayerData)
                teamData.forEach(Database::replicateTeamData)
                TreeTumblers.LOGGER.info("✅ | Autosave has finished!")

                nextAutosaveTick = Bukkit.getServer().currentTick + Constants.AUTOMATIC_REPLICATION_INTERVAL
            }
        }

        nextAutosaveTick = Bukkit.getServer().currentTick + Constants.AUTOMATIC_REPLICATION_INTERVAL
        autosave!!.runTaskTimer(TreeTumblers.plugin, Constants.AUTOMATIC_REPLICATION_INTERVAL, Constants.AUTOMATIC_REPLICATION_INTERVAL)
    }

    fun beforeunload() {
        teamData.forEach(Database::replicateTeamData)
    }

    fun playerJoin(player: Player) {
        val data = PlayerData(player)
        playerData[player] = data
    }

    fun playerLeave(player: Player) {
        val data = playerData[player]
        if(data == null) {
            TreeTumblers.LOGGER.warning("⚠️ | Player ${player.name} has no data to replicate. Aborting...")
            return
        }

        Database.replicatePlayerData(player, data)
        playerData.remove(player)
    }

    class PlayerData {
        val player: Player
        val team: String?
        val score: Int

        constructor(plr: Player) {
            player = plr

            val data: ResultSet? = Database.getPlayerDatabaseInformation(plr)
            if(data == null) {
                team = null
                score = 0
                TreeTumblers.LOGGER.severe("❌ | Player ${player.name} does not have any data associated with their account, this most likely means an error occurred along the path. Check the prior stack traces for more details.")
                return
            }

            team = data.getString("team")
            score = data.getInt("score")
            TreeTumblers.LOGGER.info("✅ | Player ${player.name} has loaded their data successfully!")
        }
    }

    class TeamData {
        val id: String
        val displayName: String
        val score: Int
        val order: Int
        val color: String
        val icon: String
        val players: MutableList<OfflinePlayer> = ArrayList()

        constructor(team: String) {
            id = team

            val data: ResultSet? = Database.getTeamData(team)
            if(data == null) {
                displayName = "Unknown"
                score = 0
                order = 999
                color = "#ffffff"
                icon = "\u0000"

                TreeTumblers.LOGGER.warning("⚠️ | Could not get team data for $team, using defaults.")
                return
            }

            displayName = data.getString("displayName")
            score = data.getInt("score")
            color = data.getString("color")
            icon = data.getString("icon")
            order = data.getInt("order")

            val members: MutableList<OfflinePlayer>? = Database.getTeamMembers(team)
            if(members == null) {
                TreeTumblers.LOGGER.warning("⚠️ | Could not get team members for $team, using an empty list.")
                return
            }

            players.addAll(members)
        }
    }
}

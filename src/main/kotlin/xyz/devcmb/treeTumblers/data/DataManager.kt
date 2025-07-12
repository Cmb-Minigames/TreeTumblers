package xyz.devcmb.treeTumblers.data

import org.bukkit.Bukkit
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

    fun startup() {
        autosave = object : BukkitRunnable() {
            override fun run() {
                TreeTumblers.LOGGER.info("ℹ️ | An autosave has started.")
                playerData.forEach(Database::replicatePlayerData)
                TreeTumblers.LOGGER.info("✅ | Autosave has finished!")

                nextAutosaveTick = Bukkit.getServer().currentTick + Constants.AUTOMATIC_REPLICATION_INTERVAL
            }
        }

        nextAutosaveTick = Bukkit.getServer().currentTick + Constants.AUTOMATIC_REPLICATION_INTERVAL
        autosave!!.runTaskTimer(TreeTumblers.plugin, Constants.AUTOMATIC_REPLICATION_INTERVAL, Constants.AUTOMATIC_REPLICATION_INTERVAL)
        // TODO: Initialize team data
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
        constructor(team: String) {

        }
    }
}

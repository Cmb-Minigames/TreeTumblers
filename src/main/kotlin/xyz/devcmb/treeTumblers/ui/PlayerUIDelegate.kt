package xyz.devcmb.treeTumblers.ui

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.devcmb.treeTumblers.Constants
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.ui.actionbar.IActionBar
import xyz.devcmb.treeTumblers.ui.actionbar.VersionDisplayBar

class PlayerUIDelegate(val player: Player) {
    val actionbars: MutableMap<String, IActionBar> = HashMap()
    var actionbarRunnable: BukkitRunnable? = null

    fun load() {
        LoadActionbars()

        if(Constants.DEV_MODE) {
            ActivateActionbar("versionDisplay")
        }
    }

    fun unload() {
        if (actionbarRunnable !== null) {
            actionbarRunnable!!.cancel()
        }
    }

    private fun LoadActionbars() {
        loadActionbar(VersionDisplayBar(player))
    }

    private fun loadActionbar(actionbar: IActionBar) {
        actionbars[actionbar.name] = actionbar
    }

    fun ActivateActionbar(name: String) {
        if(actionbarRunnable !== null) {
            actionbarRunnable!!.cancel()
            actionbarRunnable = null
        }

        val ab: IActionBar? = actionbars[name]
        if(ab != null) {
            actionbarRunnable = (object: BukkitRunnable() {
                override fun run() = ab.send()
            })

            actionbarRunnable!!.runTaskTimer(TreeTumblers.plugin, 0, 10)
        } else {
            TreeTumblers.LOGGER.info("❌ | Could not load actionbar $name for player ${player.name} because it does not exist.")
        }
    }
}
package xyz.devcmb.treeTumblers.client

import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import xyz.devcmb.treeTumblers.Constants
import xyz.devcmb.treeTumblers.TreeTumblers
import xyz.devcmb.treeTumblers.client.actionbar.IActionBar
import xyz.devcmb.treeTumblers.client.actionbar.VersionDisplayBar
import xyz.devcmb.treeTumblers.client.inventories.InventoryDefinition
import xyz.devcmb.treeTumblers.client.inventories.PlayerDataInventory

class PlayerUIDelegate(val player: Player) {
    val actionbars: MutableMap<String, IActionBar> = HashMap()
    val inventories: MutableMap<String, InventoryDefinition> = HashMap()
    var actionbarRunnable: BukkitRunnable? = null

    fun load() {
        loadActionbars()
        loadInventories()

        if(Constants.DEV_MODE) {
            activateActionbar("versionDisplay")
        }
    }

    fun unload() {
        if (actionbarRunnable !== null) {
            actionbarRunnable!!.cancel()
        }
    }

    private fun loadActionbars() {
        loadActionbar(VersionDisplayBar(player))
    }

    private fun loadInventories() {
        loadInventory(PlayerDataInventory())
    }

    private fun loadActionbar(actionbar: IActionBar) {
        actionbars[actionbar.name] = actionbar
    }

    private fun loadInventory(inventory: InventoryDefinition) {
        inventories[inventory.id] = inventory
    }

    suspend fun setInventory(id: String) {
        val inv = inventories[id]
        if(inv == null) {
            TreeTumblers.LOGGER.info("❌ | Could not load inventory $id for player ${player.name} because it does not exist.")
            return
        }

        inv.inventory.open(player)
    }

    fun activateActionbar(name: String) {
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
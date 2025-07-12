package xyz.devcmb.treeTumblers.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.devcmb.treeTumblers.client.UIManager
import xyz.devcmb.treeTumblers.data.DataManager

class PlayerConnections : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        DataManager.playerJoin(event.player)
        UIManager.playerAdded(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        UIManager.playerRemoved(event.player)
        DataManager.playerLeave(event.player)
    }
}
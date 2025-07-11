package xyz.devcmb.treeTumblers.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import xyz.devcmb.treeTumblers.ui.UIManager

class PlayerConnections : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        UIManager.PlayerAdded(event.player)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        UIManager.PlayerRemoved(event.player)
    }
}
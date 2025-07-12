package xyz.devcmb.treeTumblers.listeners

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import xyz.devcmb.treeTumblers.TreeTumblers

object ListenerManager {
    fun RegisterAllListeners() {
        registerListener(PlayerConnections())
    }

    private fun registerListener(listener: Listener) {
        val pluginManager: PluginManager = Bukkit.getPluginManager()
        pluginManager.registerEvents(listener, TreeTumblers.plugin)
    }
}
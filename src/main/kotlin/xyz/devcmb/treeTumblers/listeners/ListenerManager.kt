package xyz.devcmb.treeTumblers.listeners

import com.sun.source.tree.Tree
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import xyz.devcmb.treeTumblers.TreeTumblers

class ListenerManager {
    companion object {
        fun RegisterAllListeners() {
            registerListener(PlayerConnections())
        }

        private fun registerListener(listener: Listener) {
            val pluginManager: PluginManager = Bukkit.getPluginManager()
            pluginManager.registerEvents(listener, TreeTumblers.plugin)
        }
    }
}
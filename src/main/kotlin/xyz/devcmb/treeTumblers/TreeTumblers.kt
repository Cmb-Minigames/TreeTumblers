package xyz.devcmb.treeTumblers
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import xyz.devcmb.treeTumblers.listeners.ListenerManager
import xyz.devcmb.treeTumblers.util.Database
import java.util.logging.Logger

class TreeTumblers : JavaPlugin() {
    companion object {
        private lateinit var _plugin: Plugin
        val plugin: Plugin
            get() = _plugin
        val LOGGER: Logger
            get() = _plugin.logger
    }

    override fun onEnable() {
        _plugin = this
        saveDefaultConfig()

        Database.connect()
        ListenerManager.RegisterAllListeners()
    }

    override fun onDisable() = Database.disconnect()
}

package xyz.devcmb.treeTumblers

import org.bukkit.plugin.java.JavaPlugin
import xyz.devcmb.treeTumblers.commands.CommandManager
import xyz.devcmb.treeTumblers.data.DataManager
import xyz.devcmb.treeTumblers.listeners.ListenerManager
import xyz.devcmb.treeTumblers.util.Database
import java.util.logging.Logger

class TreeTumblers : JavaPlugin() {
    companion object {
        private lateinit var _plugin: JavaPlugin
        val plugin: JavaPlugin
            get() = _plugin
        val LOGGER: Logger
            get() = _plugin.logger
    }

    override fun onEnable() {
        _plugin = this
        saveDefaultConfig()

        Database.connect()
        DataManager.startup()
        ListenerManager.RegisterAllListeners()
        CommandManager.register()
    }

    override fun onDisable() {
        DataManager.beforeunload()
        Database.disconnect()
    }
}

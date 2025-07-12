package xyz.devcmb.treeTumblers.ui

import org.bukkit.entity.Player

object UIManager {
    var delegates: MutableMap<Player, PlayerUIDelegate> = HashMap()

    fun PlayerAdded(player: Player) {
        val delegate = PlayerUIDelegate(player)
        delegate.load()
        delegates[player] = delegate
    }

    fun PlayerRemoved(player: Player) {
        val delegate: PlayerUIDelegate? = delegates[player]
        if(delegate == null) return
        delegate.unload()
        delegates.remove(player)
    }
}
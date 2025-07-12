package xyz.devcmb.treeTumblers.client

import org.bukkit.entity.Player

object UIManager {
    var delegates: MutableMap<Player, PlayerUIDelegate> = HashMap()

    fun playerAdded(player: Player) {
        val delegate = PlayerUIDelegate(player)
        delegate.load()
        delegates[player] = delegate
    }

    fun playerRemoved(player: Player) {
        val delegate: PlayerUIDelegate? = delegates[player]
        if(delegate == null) return
        delegate.unload()
        delegates.remove(player)
    }
}
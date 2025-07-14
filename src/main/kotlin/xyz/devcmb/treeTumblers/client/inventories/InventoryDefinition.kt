package xyz.devcmb.treeTumblers.client.inventories

import com.noxcrew.interfaces.interfaces.Interface
import com.noxcrew.interfaces.pane.Pane

interface InventoryDefinition {
    val id: String
    val inventory: Interface<*, Pane>
}
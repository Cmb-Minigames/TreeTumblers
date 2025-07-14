package xyz.devcmb.treeTumblers.util

import net.kyori.adventure.text.Component
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.math.pow
import kotlin.math.sqrt

object Utilities {
    fun ItemStack.name(name: String): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.displayName(Component.text(name))
        }
        return this
    }

    fun ItemStack.name(name: Component): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.displayName(name)
        }
        return this
    }

    fun ItemStack.description(description: String): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.lore(listOf(Component.text(description)))
        }
        return this
    }

    fun ItemStack.description(description: Component): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.lore(listOf(description))
        }
        return this
    }

    fun ItemStack.description(description: List<Component>): ItemStack {
        itemMeta = itemMeta.also { meta ->
            meta.lore(description)
        }
        return this
    }

    // Yummy copilot code
    // You think I can do any of this myself?
    private val glassPaneColors = mapOf(
        Material.WHITE_STAINED_GLASS_PANE to Color.fromRGB(255, 255, 255),
        Material.ORANGE_STAINED_GLASS_PANE to Color.fromRGB(216, 127, 51),
        Material.MAGENTA_STAINED_GLASS_PANE to Color.fromRGB(178, 76, 216),
        Material.LIGHT_BLUE_STAINED_GLASS_PANE to Color.fromRGB(102, 153, 216),
        Material.YELLOW_STAINED_GLASS_PANE to Color.fromRGB(229, 229, 51),
        Material.LIME_STAINED_GLASS_PANE to Color.fromRGB(127, 204, 25),
        Material.PINK_STAINED_GLASS_PANE to Color.fromRGB(242, 127, 165),
        Material.GRAY_STAINED_GLASS_PANE to Color.fromRGB(76, 76, 76),
        Material.LIGHT_GRAY_STAINED_GLASS_PANE to Color.fromRGB(153, 153, 153),
        Material.CYAN_STAINED_GLASS_PANE to Color.fromRGB(76, 127, 153),
        Material.PURPLE_STAINED_GLASS_PANE to Color.fromRGB(127, 63, 178),
        Material.BLUE_STAINED_GLASS_PANE to Color.fromRGB(51, 76, 178),
        Material.BROWN_STAINED_GLASS_PANE to Color.fromRGB(102, 76, 51),
        Material.GREEN_STAINED_GLASS_PANE to Color.fromRGB(102, 127, 51),
        Material.RED_STAINED_GLASS_PANE to Color.fromRGB(153, 51, 51),
        Material.BLACK_STAINED_GLASS_PANE to Color.fromRGB(25, 25, 25)
    )

    fun closestGlassPane(hex: String): Material {
        val rgb = Integer.parseInt(hex.removePrefix("#"), 16)
        val inputColor = Color.fromRGB((rgb shr 16) and 0xFF, (rgb shr 8) and 0xFF, rgb and 0xFF)

        return glassPaneColors.minByOrNull { (_, color) ->
            colorDistance(inputColor, color)
        }?.key ?: Material.WHITE_STAINED_GLASS_PANE
    }

    private fun colorDistance(c1: Color, c2: Color): Double {
        return sqrt(
            (c1.red - c2.red).toDouble().pow(2) +
                    (c1.green - c2.green).toDouble().pow(2) +
                    (c1.blue - c2.blue).toDouble().pow(2)
        )
    }
}
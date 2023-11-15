package kr.cosine.fishing.event

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class HQFishingEvent(
    val fishingPlayer: Player,
    val fish: ItemStack
) : PlayerEvent(fishingPlayer) {

    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }

    override fun getHandlers(): HandlerList = getHandlerList()
}
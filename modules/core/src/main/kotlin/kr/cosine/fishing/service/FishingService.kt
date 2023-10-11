package kr.cosine.fishing.service

import kr.cosine.fishing.enums.AnnounceType
import kr.cosine.fishing.registry.HookRegistry
import kr.cosine.fishing.registry.MessageRegistry
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.FishHook
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.util.Vector
import java.util.UUID

@Service
class FishingService(
    private val messageRegistry: MessageRegistry,
    private val hookRegistry: HookRegistry
) {

    private val biteCountMap = mutableMapOf<UUID, Int>()

    fun onFishing(event: PlayerFishEvent) {
        when (event.state) {
            PlayerFishEvent.State.REEL_IN -> reelIn(event)
            PlayerFishEvent.State.FISHING -> fishing(event)
            PlayerFishEvent.State.BITE -> bite(event)
            else -> event.hook.waitTime = hookRegistry.getWaitTime()
        }
    }

    private fun reelIn(event: PlayerFishEvent) {

    }

    private fun fishing(event: PlayerFishEvent) {

    }

    private fun bite(event: PlayerFishEvent) {
        event.isCancelled = true

        val player = event.player
        val playerUniqueId = player.uniqueId

        val hook = event.hook

        val biteCount = biteCountMap[playerUniqueId] ?: 0
        val randomTick = hookRegistry.getTick()
        val pressPower = hookRegistry.pressPower

        if (biteCount >= hookRegistry.maxBite || hookRegistry.isBiteChance()) {
            hook.pressDown(pressPower.real)
            biteCountMap.remove(playerUniqueId)
        } else {
            hook.pressDown(pressPower.fake)
            biteCountMap[playerUniqueId] = biteCount + 1
            messageRegistry[AnnounceType.FAKE_BITE]?.playSound(player)
        }
    }

    private fun FishHook.pressDown(power: Double) {
        velocity = Vector(0, -1, 0).normalize().multiply(power)
    }
}
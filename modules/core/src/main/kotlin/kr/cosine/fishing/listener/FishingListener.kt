package kr.cosine.fishing.listener

import kr.cosine.fishing.service.FishingService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerJoinEvent

@Listener
class FishingListener(
    private val fishingService: FishingService
) {

    @Subscribe
    fun onJoin(event: PlayerJoinEvent) {
        fishingService.initVirtual(event.player)
    }

    @Subscribe
    fun onFish(event: PlayerFishEvent) {
        fishingService.onFishing(event)
    }
}
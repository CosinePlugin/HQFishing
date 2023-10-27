package kr.cosine.fishing.listener

import kr.cosine.fishing.service.FishingService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerFishEvent

@Listener
class FishingListener(
    private val fishingService: FishingService
) {

    @Subscribe
    fun onFish(event: PlayerFishEvent) {
        fishingService.onFishing(event)
    }
}
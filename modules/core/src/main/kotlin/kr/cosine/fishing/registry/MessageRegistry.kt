package kr.cosine.fishing.registry

import kr.cosine.fishing.announce.FishingAnnounce
import kr.cosine.fishing.enums.AnnounceType
import kr.hqservice.framework.global.core.component.Bean

@Bean
class MessageRegistry {

    private val messageMap = mutableMapOf<AnnounceType, FishingAnnounce>()

    operator fun get(announceType: AnnounceType): FishingAnnounce? = messageMap[announceType]

    operator fun set(announceType: AnnounceType, fishingAnnounce: FishingAnnounce) {
        messageMap[announceType] = fishingAnnounce
    }

    internal fun clear() {
        messageMap.clear()
    }
}
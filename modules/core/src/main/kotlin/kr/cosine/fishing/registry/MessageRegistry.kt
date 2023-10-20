package kr.cosine.fishing.registry

import kr.cosine.fishing.announce.FishingAnnounce
import kr.cosine.fishing.enums.AnnounceType
import kr.hqservice.framework.global.core.component.Bean

@Bean
class MessageRegistry {

    private val messageMap = mutableMapOf<AnnounceType, FishingAnnounce>()

    fun get(announceType: AnnounceType): FishingAnnounce {
        return messageMap[announceType] ?: throw IllegalArgumentException("${announceType}이(가) 등록되어 있지 않습니다.")
    }

    fun set(announceType: AnnounceType, fishingAnnounce: FishingAnnounce) {
        messageMap[announceType] = fishingAnnounce
    }

    internal fun clear() {
        messageMap.clear()
    }
}
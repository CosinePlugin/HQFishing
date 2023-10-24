package kr.cosine.fishing.registry

import kr.cosine.fishing.announce.FishingAnnounce
import kr.cosine.fishing.enums.AnnounceType
import kr.hqservice.framework.global.core.component.Bean

@Bean
class AnnounceRegistry {

    private val announceMap = mutableMapOf<AnnounceType, FishingAnnounce>()

    fun get(announceType: AnnounceType): FishingAnnounce {
        return announceMap[announceType] ?: throw IllegalArgumentException("${announceType}이(가) 등록되어 있지 않습니다.")
    }

    fun set(announceType: AnnounceType, fishingAnnounce: FishingAnnounce) {
        announceMap[announceType] = fishingAnnounce
    }

    internal fun clear() {
        announceMap.clear()
    }
}
package kr.cosine.fishing.registry

import kr.hqservice.framework.global.core.component.Bean
import java.util.UUID

@Bean
class TickRegistry {

    private val tickMap = mutableMapOf<UUID, Pair<Long, Long>>()

    fun contains(uniqueId: UUID): Boolean = tickMap.containsKey(uniqueId)

    fun get(uniqueId: UUID): Long {
        val pair = tickMap[uniqueId] ?: return -1
        return if (pair.second <= System.currentTimeMillis()) -1 else pair.first
    }

    fun set(uniqueId: UUID, tick: Long) {
        tickMap[uniqueId] = tick to (System.currentTimeMillis() + tick * 50)
    }

    fun remove(uniqueId: UUID) {
        tickMap.remove(uniqueId)
    }
}
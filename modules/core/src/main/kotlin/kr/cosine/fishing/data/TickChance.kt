package kr.cosine.fishing.data

import kr.cosine.fishing.extension.random

class TickChance {

    private val tickChanceMap = mutableMapOf<Long, Double>()

    fun getTick(): Long = tickChanceMap.random()

    fun setTick(tick: Long, chance: Double) {
        tickChanceMap[tick] = chance
    }

    internal fun clear() {
        tickChanceMap.clear()
    }
}
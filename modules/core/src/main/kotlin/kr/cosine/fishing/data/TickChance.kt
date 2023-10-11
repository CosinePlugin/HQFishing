package kr.cosine.fishing.data

import kr.cosine.fishing.extension.random

class TickChance {

    private val tickChanceMap = mutableMapOf<Int, Double>()

    fun getTick(): Int = tickChanceMap.random()

    fun setTick(tick: Int, chance: Double) {
        tickChanceMap[tick] = chance
    }

    internal fun clear() {
        tickChanceMap.clear()
    }
}
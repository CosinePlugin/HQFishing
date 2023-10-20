package kr.cosine.fishing.registry

import kr.cosine.fishing.data.PressPower
import kr.cosine.fishing.data.TickChance
import kr.cosine.fishing.extension.chance
import kr.hqservice.framework.global.core.component.Bean

@Bean
class HookRegistry {

    private var waitTimeRange = 2..4

    private var biteChance = 30.0

    var maxBite = 5
        private set

    var pressPower = PressPower()
        private set

    private val tickChance = TickChance()

    fun getWaitTime(): Int = waitTimeRange.random()

    fun setWaitTime(min: Int, max: Int) {
        waitTimeRange = min..max
    }

    fun isBiteChance(): Boolean = biteChance.chance()

    fun setBiteChance(biteChance: Double) {
        this.biteChance = biteChance
    }

    fun setMaxBite(maxBite: Int) {
        this.maxBite = maxBite
    }

    fun setPressPower(pressPower: PressPower) {
        this.pressPower = pressPower
    }

    fun getTick(): Long = tickChance.getTick()

    fun setTick(tick: Long, chance: Double) {
        tickChance.setTick(tick, chance)
    }

    internal fun clear() {
        tickChance.clear()
    }
}
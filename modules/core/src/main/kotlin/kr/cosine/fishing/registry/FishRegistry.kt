package kr.cosine.fishing.registry

import kr.cosine.fishing.fish.Fish
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.entity.FishHook

@Bean
class FishRegistry {

    private val fishMap = mutableMapOf<String, Fish>()

    fun findByKey(key: String): Fish? = fishMap[key]

    fun getKeys(): List<String> = getMap().keys.toList()

    fun getValues(): List<Fish> = getMap().values.toList()

    fun getMap(): Map<String, Fish> = fishMap

    fun getCatchableFishes(hook: FishHook, tick: Long): Map<Fish, Double> {
        return fishMap.values.filter { it.isCatchable(hook, tick) }.associateWith { it.chance }
    }
}
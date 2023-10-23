package kr.cosine.fishing.fish

import kr.cosine.fishing.enums.Weather
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.entity.FishHook
import org.bukkit.inventory.ItemStack

class Fish(
    val key: String,
    private val itemStack: ItemStack
) {

    var biomes = emptyList<Biome>()
        private set

    var weather = Weather.ALL
        private set

    var time = 0..23999
        private set

    var tick = 30L
        private set

    var chance = 100.0
        private set

    var isChanged = false

    fun isBiome(biome: Biome): Boolean {
        return biomes.isEmpty() || biomes.contains(biome)
    }

    fun setBiomes(biomes: List<Biome>) {
        this.biomes = biomes
        isChanged = true
    }

    private fun isWeather(world: World): Boolean {
        return when (weather) {
            Weather.ALL -> true
            Weather.SUN -> world.isClearWeather
            Weather.RAIN -> !world.isClearWeather
        }
    }

    fun switchWeather() {
        weather = weather.switch()
        isChanged = true
    }

    fun isTime(world: World): Boolean {
        return time.contains(world.time)
    }

    fun setTime(min: Int, max: Int) {
        time = min..max
        isChanged = true
    }

    fun setTick(tick: Long) {
        this.tick = tick
        isChanged = true
    }

    fun setChance(chance: Double) {
        this.chance = chance
        isChanged = true
    }

    fun isCatchable(hook: FishHook, tick: Long): Boolean {
        val world = hook.world
        val biome = hook.location.block.biome
        return isWeather(world) && isTime(world) && isBiome(biome) && this.tick >= tick
    }

    fun getItemStack(): ItemStack = itemStack.clone()
}
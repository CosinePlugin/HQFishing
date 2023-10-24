package kr.cosine.fishing.config

import kotlinx.coroutines.launch
import kr.cosine.fishing.enums.Weather
import kr.cosine.fishing.fish.Fish
import kr.cosine.fishing.registry.FishRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.block.Biome
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

@Bean
class FishConfig(
    private val plugin: HQBukkitPlugin,
    private val fishRegistry: FishRegistry
) {

    private val fishFolder = File(plugin.dataFolder, "fish").apply { if (!exists()) mkdirs() }

    fun load() {
        fishFolder.listFiles()?.forEach { file ->
            val key = file.name.removeSuffix(".yml")
            val config = YamlConfiguration.loadConfiguration(file)

            val itemStack = config.getItemStack("item")!!
            val chance = config.getDouble("chance")
            val weather = Weather.valueOf(config.getString("weather") ?: "ALL")
            val biomes = config.getStringList("biome").map { Biome.valueOf(it) }
            val tick = config.getLong("tick")
            val minTime = config.getInt("time.min")
            val maxTime = config.getInt("time.max")

            val fish = Fish(key, itemStack).apply {
                setChance(chance)
                setWeather(weather)
                setBiomes(biomes)
                setTick(tick)
                setTime(minTime, maxTime)
                isChanged = false
            }
            fishRegistry.set(key, fish)
        }
    }

    fun save() {
        fishRegistry.getMap().filter { it.value.isChanged }.forEach { (key, fish) ->
            val file = File(fishFolder, "$key.yml")
            val config = YamlConfiguration.loadConfiguration(file)

            config.set("item", fish.getItemStack())
            config.set("chance", fish.chance)
            config.set("weather", fish.weather.name)
            config.set("biomes", fish.biomes.map { it.name })
            config.set("tick", fish.tick)
            config.set("time.min", fish.time.first)
            config.set("time.max", fish.time.last)

            config.save(file)
            fish.isChanged = false
        }
    }

    fun delete(key: String) {
        fishRegistry.remove(key)
        plugin.launch {
            File(fishFolder, "$key.yml").delete()
        }
    }
}
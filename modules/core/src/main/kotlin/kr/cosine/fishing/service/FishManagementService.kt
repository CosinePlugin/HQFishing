package kr.cosine.fishing.service

import kotlinx.coroutines.launch
import kr.cosine.fishing.config.FishConfig
import kr.cosine.fishing.config.FishingConfig
import kr.cosine.fishing.fish.Fish
import kr.cosine.fishing.inventory.FishSettingView
import kr.cosine.fishing.inventory.FishSettingViewModel
import kr.cosine.fishing.registry.FishRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Service
class FishManagementService(
    private val plugin: HQBukkitPlugin,
    private val fishingConfig: FishingConfig,
    private val fishConfig: FishConfig,
    private val fishRegistry: FishRegistry,
    private val fishSettingViewModel: FishSettingViewModel
) {

    fun registerFish(key: String, itemStack: ItemStack): Boolean? {
        if (fishRegistry.contains(key)) return null
        if (itemStack.type.isAir) return false
        val fish = Fish(key, itemStack)
        fish.isChanged = true
        fishRegistry.set(key, fish)
        return true
    }

    fun deleteFish(key: String): Boolean {
        if (!fishRegistry.contains(key)) return false
        fishConfig.delete(key)
        return true
    }

    fun openFishSettingView(player: Player) {
        FishSettingView(fishSettingViewModel).open(player)
    }

    fun save() {
        plugin.launch {
            fishConfig.save()
        }
    }

    fun reload() {
        fishingConfig.reload()
    }
}
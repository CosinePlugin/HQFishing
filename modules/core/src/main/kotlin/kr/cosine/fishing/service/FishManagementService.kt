package kr.cosine.fishing.service

import kr.cosine.fishing.fish.Fish
import kr.cosine.fishing.inventory.FishSettingView
import kr.cosine.fishing.inventory.FishSettingViewModel
import kr.cosine.fishing.registry.FishRegistry
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

@Service
class FishManagementService(
    private val fishRegistry: FishRegistry,
    private val fishSettingViewModel: FishSettingViewModel
) {

    fun setFish(key: String, itemStack: ItemStack) {
        fishRegistry.set(key, Fish(key, itemStack))
    }

    fun openFishSettingView(player: Player) {
        FishSettingView(fishSettingViewModel).open(player)
    }
}
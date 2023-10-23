package kr.cosine.fishing.inventory

import kr.cosine.fishing.fish.Fish
import kr.hqservice.framework.inventory.container.HQContainer
import org.bukkit.inventory.Inventory

class FishDetailSettingView(
    private val fish: Fish
) : HQContainer(54, "") {

    override fun initialize(inventory: Inventory) {

    }
}
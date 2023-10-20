package kr.cosine.fishing.inventory

import kr.hqservice.framework.inventory.container.HQContainer
import org.bukkit.inventory.Inventory

class FishSettingView(
    private val fishSettingViewModel: FishSettingViewModel
) : HQContainer(54, "물고기 설정", true) {

    override fun initialize(inventory: Inventory) {
        fishSettingViewModel.getBeforePageButton().setSlot(this, 45)
        fishSettingViewModel.getBackgroundPageButton().setSlot(this, 46..52)
        fishSettingViewModel.getNextPageButton().setSlot(this, 53)
    }
}
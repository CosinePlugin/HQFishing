package kr.cosine.fishing.inventory

import kr.cosine.fishing.fish.Fish
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.container.HQContainer
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class FishSettingView(
    private val fishSettingViewModel: FishSettingViewModel,
    override var page: Int = 0
) : FishPageContainer<Fish>(54, "물고기 설정", true) {

    private companion object {
        val air = ItemStack(Material.AIR)
    }

    override val baseList = fishSettingViewModel.getFishes()
    
    override fun initialize(inventory: Inventory) {
        (0..44).forEach { slot ->
            inventory.setItem(slot, air)
        }
        if (chunkedList.isNotEmpty()) {
            currentPageList.forEachIndexed { index, fish ->
                val fishKey = fish.key
                val fishItemStack = fish.getItemStack()
                fishItemStack.editMeta {
                    it.setDisplayName("§7[$fishKey] §f${fishItemStack.getDisplayName()}")
                    it.lore = listOf("", "§d[ 클릭 시 세부 설정으로 이동합니다. ]")
                }
                inventory.setItem(index, fishItemStack)
            }
        }
        fishSettingViewModel.getBackgroundButton().setSlot(this, 46..52)
        fishSettingViewModel.getBeforePageButton<Fish>().setSlot(this, 45)
        fishSettingViewModel.getNextPageButton<Fish>().setSlot(this, 53)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return

        val slot = event.rawSlot
        if (slot > 44 || slot >= currentPageList.size) return

        val player = event.whoClicked as Player
        val fish = currentPageList[slot]

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)

        player.closeInventory()
        fishSettingViewModel.runTaskLater {
            FishDetailSettingView(fishSettingViewModel, this, fish).open(player)
        }
    }
}
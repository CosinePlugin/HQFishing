package kr.cosine.fishing.inventory

import kr.cosine.fishing.fish.Fish
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Biome
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class FishBiomeSettingView(
    private val fishSettingViewModel: FishSettingViewModel,
    private val fishDetailSettingView: FishDetailSettingView,
    private val fish: Fish,
    override var page: Int = 0
) : FishPageContainer<Biome>(54, "${fish.getItemStack().getDisplayName()} : 바이옴 설정", true) {

    private companion object {
        val air = ItemStack(Material.AIR)
    }

    override val baseList = Biome.values().toList()

    private val fishBiomes = fish.biomes.toMutableList()

    override fun initialize(inventory: Inventory) {
        (0..44).forEach { slot ->
            inventory.setItem(slot, air)
        }
        if (chunkedList.isNotEmpty()) {
            currentPageList.forEachIndexed { index, biome ->
                val material = if (fishBiomes.contains(biome)) {
                    Material.LIME_STAINED_GLASS_PANE
                } else {
                    Material.RED_STAINED_GLASS_PANE
                }
                val itemStack = ItemStack(material)
                itemStack.editMeta {
                    it.setDisplayName("§f${biome.name}")
                }
                inventory.setItem(index, itemStack)
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
        val biome = currentPageList[slot]

        if (fishBiomes.contains(biome)) {
            fishBiomes.remove(biome)
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 0f)
        } else {
            fishBiomes.add(biome)
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
        }
        refresh()
    }

    override fun onClose(event: InventoryCloseEvent) {
        val player = event.player as Player
        fish.setBiomes(fishBiomes)
        fishSettingViewModel.reopen(fishDetailSettingView, player)
    }
}
package kr.cosine.fishing.inventory

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
    private var page: Int
) : HQContainer(54, "물고기 설정", true) {

    private companion object {
        val air = ItemStack(Material.AIR)
    }

    private val fishes = fishSettingViewModel.getFishes()
    private val chunkedFishes get() = fishes.chunked(45)
    private val currentPageFishes get() = chunkedFishes[page]
    
    override fun initialize(inventory: Inventory) {
        (0..44).forEach { slot ->
            inventory.setItem(slot, air)
        }
        if (chunkedFishes.isNotEmpty()) {
            currentPageFishes.forEachIndexed { index, fish ->
                val fishKey = fish.key
                val fishItemStack = fish.getItemStack()
                fishItemStack.editMeta {
                    it.setDisplayName("§7[$fishKey] §f${fishItemStack.getDisplayName()}")
                    it.lore = listOf("", "§d[ 클릭 시 세부 설정으로 이동합니다.")
                }
                inventory.setItem(index, fishItemStack)
            }
        }
        // 페이지 배경
        HQButtonBuilder(Material.WHITE_STAINED_GLASS_PANE)
            .setDisplayName("§f")
            .build()
            .setSlot(this, 46..48, 50..52)
        // 이전 페이지
        HQButtonBuilder(Material.RED_STAINED_GLASS_PANE).apply {
            setDisplayName("§c이전 페이지로 이동")
            setClickFunction { event ->
                val player = event.getWhoClicked()
                player.playButtonSound()
                if (page == 0) {
                    player.sendMessage("§c이전 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                page--
                refresh()
            }
        }.build().setSlot(this, 45)
        // 다음 페이지
        HQButtonBuilder(Material.GREEN_STAINED_GLASS_PANE).apply {
            setDisplayName("§a다음 페이지로 이동")
            setClickFunction { event ->
                val player = event.getWhoClicked()
                player.playButtonSound()
                if (page + 1 >= chunkedFishes.size) {
                    player.sendMessage("§c다음 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                page++
                refresh()
            }
        }.build().setSlot(this, 53)
    }

    private fun Player.playButtonSound(volume: Float = 1f, pitch: Float = 1f) {
        playSound(this, Sound.UI_BUTTON_CLICK, volume, pitch)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return

        val slot = event.rawSlot
        if (slot > 44 || slot < currentPageFishes.size) return

        val player = event.whoClicked as Player
        val fish = currentPageFishes[slot]

        fishSettingViewModel.runTaskLater {
            FishDetailSettingView(fish).open(player)
        }
    }
}
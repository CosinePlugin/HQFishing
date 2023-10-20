package kr.cosine.fishing.inventory

import kr.cosine.fishing.extension.runTaskLater
import kr.cosine.fishing.registry.FishRegistry
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.inventory.button.HQButton
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Material
import org.bukkit.plugin.Plugin

@Bean
class FishSettingViewModel(
    private val plugin: Plugin,
    private val fishRegistry: FishRegistry
) {

    fun loopFishSettingButton(buttonFunction: (Int, HQButton) -> Unit) {
        var slot = 0
        fishRegistry.getMap().forEach { (key, fish) ->
            val fishItemStack = fish.getItemStack()
            val button = HQButtonBuilder(fishItemStack).apply {
                setDisplayName("§7[$key] §f${fishItemStack.getDisplayName()}")
                setLore(listOf("", "§d[ 클릭 시 세부 설정으로 이동합니다."))
                setClickFunction { event ->
                    val player = event.getWhoClicked()
                    plugin.runTaskLater {
                        FishDetailSettingView().open(player)
                    }
                }
            }.build()
            buttonFunction(slot, button)
            slot++
        }
    }

    fun getBackgroundPageButton(): HQButton {
        return HQButtonBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§f").build()
    }

    fun getBeforePageButton(): HQButton {
        return HQButtonBuilder(Material.RED_STAINED_GLASS_PANE).apply {

        }.build()
    }

    fun getNextPageButton(): HQButton {
        return HQButtonBuilder(Material.GREEN_STAINED_GLASS_PANE).apply {

        }.build()
    }
}
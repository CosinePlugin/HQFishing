package kr.cosine.fishing.inventory

import kr.cosine.fishing.extension.runTaskLater
import kr.cosine.fishing.fish.Fish
import kr.cosine.fishing.registry.FishRegistry
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.inventory.button.HQButton
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.container.HQContainer
import kr.hqservice.framework.inventory.event.ButtonClickEvent
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.plugin.Plugin

@Bean
class FishSettingViewModel(
    private val plugin: Plugin,
    private val fishRegistry: FishRegistry
) {

    internal companion object {
        fun HQButtonBuilder.setClickFunctionOnlyLeftClick(clickFunction: (ButtonClickEvent) -> Unit): HQButtonBuilder {
            return setClickFunction { event ->
                if (event.getClickType() != ClickType.LEFT) return@setClickFunction
                clickFunction(event)
            }
        }
    }

    fun getFishes(): List<Fish> = fishRegistry.getValues()

    fun runTaskLater(actionFunction: () -> Unit) {
        plugin.runTaskLater(actionFunction = actionFunction)
    }

    fun reopen(container: HQContainer, player: Player) {
        runTaskLater {
            container.open(player)
            container.refresh()
        }
    }

    fun getBackgroundButton(): HQButton {
        return HQButtonBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§f").build()
    }

    fun <T> getBeforePageButton(): HQButton {
        return HQButtonBuilder(Material.RED_STAINED_GLASS_PANE).apply {
            setDisplayName("§c이전 페이지로 이동")
            setClickFunction { event ->
                val player = event.getWhoClicked()
                val fishPageContainer = event.getContainer() as FishPageContainer<T>
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
                if (fishPageContainer.page == 0) {
                    player.sendMessage("§c이전 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                fishPageContainer.page--
                event.getContainer().refresh()
            }
        }.build()
    }

    fun <T> getNextPageButton(): HQButton {
        return HQButtonBuilder(Material.GREEN_STAINED_GLASS_PANE).apply {
            setDisplayName("§a다음 페이지로 이동")
            setClickFunction { event ->
                val player = event.getWhoClicked()
                val fishPageContainer = event.getContainer() as FishPageContainer<T>
                player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
                if (fishPageContainer.page + 1 >= fishPageContainer.chunkedList.size) {
                    player.sendMessage("§c다음 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                fishPageContainer.page++
                event.getContainer().refresh()
            }
        }.build()
    }
}
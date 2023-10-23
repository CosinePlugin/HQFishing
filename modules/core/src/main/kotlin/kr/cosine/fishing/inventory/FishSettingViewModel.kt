package kr.cosine.fishing.inventory

import kr.cosine.fishing.extension.runTaskLater
import kr.cosine.fishing.fish.Fish
import kr.cosine.fishing.registry.FishRegistry
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin

@Bean
class FishSettingViewModel(
    private val plugin: Plugin,
    private val fishRegistry: FishRegistry
) {

    fun getFishes(): List<Fish> = fishRegistry.getValues()

    fun runTaskLater(actionFunction: () -> Unit) {
        plugin.runTaskLater(actionFunction = actionFunction)
    }
}
package kr.cosine.fishing

import kr.cosine.fishing.config.FishingConfig
import kr.cosine.fishing.util.SignEditor
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule
import org.bukkit.plugin.Plugin

@Component
class HQFishingModule(
    private val plugin: HQBukkitPlugin,
    private val fishingConfig: FishingConfig
) : HQModule {

    internal companion object {
        lateinit var plugin: Plugin
            private set
    }

    override fun onEnable() {
        HQFishingModule.plugin = plugin
        SignEditor.register()
        fishingConfig.load()
    }
}
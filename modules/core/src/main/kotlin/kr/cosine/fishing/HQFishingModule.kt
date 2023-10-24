package kr.cosine.fishing

import kr.cosine.fishing.config.FishConfig
import kr.cosine.fishing.config.FishingConfig
import kr.cosine.fishing.scheduler.FishSaveScheduler
import kr.cosine.fishing.util.SignEditor
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule
import org.bukkit.plugin.Plugin

@Component
class HQFishingModule(
    private val plugin: HQBukkitPlugin,
    private val fishingConfig: FishingConfig,
    private val fishConfig: FishConfig
) : HQModule {

    internal companion object {
        lateinit var plugin: Plugin
            private set
    }

    override fun onEnable() {
        HQFishingModule.plugin = plugin
        SignEditor.register()

        fishingConfig.load()
        fishConfig.load()

        FishSaveScheduler(fishConfig).runTaskTimerAsynchronously(plugin, 3600, 3600)
    }

    override fun onDisable() {
        fishConfig.save()
    }
}
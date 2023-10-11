package kr.cosine.fishing

import kr.cosine.fishing.config.FishingConfig
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule

@Component
class HQFishingModule(
    private val fishingConfig: FishingConfig
) : HQModule {

    override fun onEnable() {
        fishingConfig.load()
    }
}
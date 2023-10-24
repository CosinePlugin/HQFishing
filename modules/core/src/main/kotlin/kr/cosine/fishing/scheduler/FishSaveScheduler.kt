package kr.cosine.fishing.scheduler

import kr.cosine.fishing.config.FishConfig
import org.bukkit.scheduler.BukkitRunnable

class FishSaveScheduler(
    private val fishConfig: FishConfig
) : BukkitRunnable() {

    override fun run() {
        fishConfig.save()
    }
}
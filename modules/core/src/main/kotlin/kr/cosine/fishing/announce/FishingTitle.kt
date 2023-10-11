package kr.cosine.fishing.announce

import org.bukkit.entity.Player

data class FishingTitle(
    private val enabled: Boolean,
    private val title: String,
    private val subTitle: String,
    private val fadeIn: Int,
    private val duration: Int,
    private val fadeOut: Int
) {

    fun sendTitle(player: Player, fish: String) {
        if (enabled) {
            player.sendTitle(
                title.replace("%fish%", fish),
                subTitle.replace("%fish%", fish),
                fadeIn,
                duration,
                fadeOut
            )
        }
    }
}
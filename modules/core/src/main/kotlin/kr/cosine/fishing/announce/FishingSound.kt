package kr.cosine.fishing.announce

import org.bukkit.entity.Player

data class FishingSound(
    private val enabled: Boolean,
    private val sound: String,
    private val volume: Float,
    private val pitch: Float
) {

    fun playSound(player: Player) {
        if (enabled) {
            player.playSound(player.location, sound, volume, pitch)
        }
    }
}
package kr.cosine.fishing.announce

import org.bukkit.entity.Player

data class FishingAnnounce(
    private val fishingSound: FishingSound,
    private val chat: FishingChat,
    private val title: FishingTitle? = null
) {

    val text get() = chat.message

    fun playSound(player: Player) {
        fishingSound.playSound(player)
    }

    fun sendMessage(player: Player, fish: String) {
        chat.sendMessage(player, fish)
    }

    fun sendTitle(player: Player, fish: String) {
        title?.sendTitle(player, fish)
    }
}
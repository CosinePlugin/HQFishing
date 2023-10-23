package kr.cosine.fishing.util

import kr.cosine.fishing.HQFishingModule.Companion.plugin
import kr.cosine.fishing.extension.runTaskLater
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.SignChangeEvent
import java.util.UUID

class SignEditor(
    val player: Player,
    val texts: Array<String>,
    val response: (Array<String>) -> Boolean
) {

    internal companion object {
        private val receivers = mutableMapOf<UUID, SignEditor>()

        fun register() {
            plugin.server.pluginManager.registerEvents(object : Listener {
                @EventHandler
                fun onSignEdit(event: SignChangeEvent) {
                    val player = event.player
                    val extraSign = receivers.remove(player.uniqueId) ?: return

                    if (extraSign.response(event.lines)) {
                        extraSign.signLocation.block.type = extraSign.originalBlock
                    } else {
                        plugin.runTaskLater { extraSign.open() }
                    }
                }
            }, plugin)
        }
    }

    private val playerLocation = player.location

    private val signLocation = Location(player.world, playerLocation.x, playerLocation.y, playerLocation.z)
    private val signBlock = signLocation.block

    private var originalBlock = signBlock.type

    fun open() {
        signBlock.type = Material.OAK_SIGN

        val state = signBlock.state
        val sign = state as Sign

        texts.forEachIndexed { index, text ->
            state.setLine(index, text)
        }
        sign.update(true)

        plugin.runTaskLater(2) {
            player.openSign(sign)
            receivers[player.uniqueId] = this
        }
    }
}
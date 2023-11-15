package kr.cosine.fishing.service

import kr.cosine.fishing.enums.AnnounceType
import kr.cosine.fishing.event.HQFishingEvent
import kr.cosine.fishing.extension.random
import kr.cosine.fishing.registry.FishRegistry
import kr.cosine.fishing.registry.HookRegistry
import kr.cosine.fishing.registry.AnnounceRegistry
import kr.cosine.fishing.registry.TickRegistry
import kr.hqservice.framework.bukkit.core.coroutine.bukkitDelay
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.nms.extension.getDisplayName
import kr.hqservice.framework.nms.extension.virtual
import kr.hqservice.framework.nms.virtual.entity.VirtualArmorStand
import org.bukkit.Location
import org.bukkit.entity.FishHook
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.plugin.PluginManager
import org.bukkit.util.Vector
import java.util.UUID

@Service
class FishingService(
    private val pluginManager: PluginManager,
    private val announceRegistry: AnnounceRegistry,
    private val hookRegistry: HookRegistry,
    private val fishRegistry: FishRegistry,
    private val tickRegistry: TickRegistry
) {

    private val biteCountMap = mutableMapOf<UUID, Int>()

    fun onFishing(event: PlayerFishEvent) {
        event.hook.waitTime = hookRegistry.getWaitTime()
        when (event.state) {
            PlayerFishEvent.State.REEL_IN -> reelIn(event)
            PlayerFishEvent.State.FISHING -> fishing(event)
            PlayerFishEvent.State.BITE -> bite(event)
            else -> {}
        }
    }

    private fun reelIn(event: PlayerFishEvent) {
        val player = event.player
        val playerUniqueId = player.uniqueId
        val tick = tickRegistry.get(playerUniqueId)
        if (tick > 2L) {
            val fishMap = fishRegistry.getCatchableFishes(event.hook, tick)
            if (fishMap.isEmpty()) {
                player.sendMessage("§c물고기가 설정되어 있지 않습니다.")
            } else {
                val fish = fishMap.random()
                val fishItemStack = fish.getItemStack()
                val fishName = fishItemStack.getDisplayName()
                announceRegistry.get(AnnounceType.SUCCESSFUL).apply {
                    sendTitle(player, fishName)
                    sendMessage(player, fishName)
                    playSound(player)
                }
                player.inventory.addItem(fishItemStack)
                val hqFishingEvent = HQFishingEvent(player, fishItemStack)
                pluginManager.callEvent(hqFishingEvent)
            }
            tickRegistry.remove(playerUniqueId)
        }
    }

    private fun fishing(event: PlayerFishEvent) {
        val player = event.player
        val playerUniqueId = player.uniqueId
        biteCountMap.remove(playerUniqueId)
        tickRegistry.remove(playerUniqueId)
    }

    private fun bite(event: PlayerFishEvent) {
        event.isCancelled = true

        val player = event.player
        val playerUniqueId = player.uniqueId

        val hook = event.hook
        val hookLocation = hook.location

        val biteCount = biteCountMap[playerUniqueId] ?: 0
        val randomTick = hookRegistry.getTick()
        val pressPower = hookRegistry.pressPower

        if (biteCount >= hookRegistry.maxBite || hookRegistry.isBiteChance()) {
            val realBiteAnnounce = announceRegistry.get(AnnounceType.REAL_BITE)
            biteCountMap.remove(playerUniqueId)
            tickRegistry.set(playerUniqueId, randomTick)
            hook.pressDown(pressPower.real)
            player.showText(hookLocation, realBiteAnnounce.text, randomTick)
            realBiteAnnounce.playSound(player)
        } else {
            val fakeBiteAnnounce = announceRegistry.get(AnnounceType.FAKE_BITE)
            biteCountMap[playerUniqueId] = biteCount + 1
            hook.pressDown(pressPower.fake)
            player.showText(hookLocation, fakeBiteAnnounce.text, randomTick)
            fakeBiteAnnounce.playSound(player)
        }
    }

    private fun FishHook.pressDown(power: Double) {
        velocity = Vector(0, -1, 0).normalize().multiply(power)
    }

    private fun Player.showText(location: Location, text: String, tick: Long) {
        virtual {
            val newLocation = location.clone().add(0.0, 0.5, 0.0)
            val virtualArmorStand = VirtualArmorStand(newLocation, text) {
                setMarker(true)
                setInvisible(true)
                setSmall(true)
            }
            updateEntity(virtualArmorStand)
            bukkitDelay(tick)
            virtualArmorStand.destroy()
            updateEntity(virtualArmorStand)
        }
    }
}
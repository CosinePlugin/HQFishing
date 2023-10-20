package kr.cosine.fishing.config

import kr.cosine.fishing.announce.FishingAnnounce
import kr.cosine.fishing.announce.FishingChat
import kr.cosine.fishing.announce.FishingSound
import kr.cosine.fishing.announce.FishingTitle
import kr.cosine.fishing.data.PressPower
import kr.cosine.fishing.enums.AnnounceType
import kr.cosine.fishing.registry.HookRegistry
import kr.cosine.fishing.registry.MessageRegistry
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

@Bean
class FishingConfig(
    private val plugin: Plugin,
    private val logger: Logger,
    private val hookRegistry: HookRegistry,
    private val messageRegistry: MessageRegistry
) {

    private val config = plugin.config

    fun load() {
        loadHookSection()
        loadMessageSection()
    }

    private fun loadHookSection() {
        val hookSectionKey = "hook"
        config.getConfigurationSection(hookSectionKey)?.apply {
            val waitTimeSectionKey = "wait-time"
            val waitTimeSection = getConfigurationSection(waitTimeSectionKey) ?: run {
                sendSectionErrorLog(hookSectionKey, waitTimeSectionKey)
                return
            }
            val minWaitTime = waitTimeSection.getInt("hook.wait-time.min")
            val maxWaitTime = waitTimeSection.getInt("hook.wait-time.max")
            hookRegistry.setWaitTime(minWaitTime, maxWaitTime)
            val biteSectionKey = "bite"
            val biteSection = getConfigurationSection(biteSectionKey) ?: run {
                sendSectionErrorLog(hookSectionKey, biteSectionKey)
                return
            }
            val pressPowerSectionKey = "press-power"
            val pressPowerSection = biteSection.getConfigurationSection(pressPowerSectionKey) ?: run {
                sendSectionErrorLog("$hookSectionKey.$biteSectionKey", pressPowerSectionKey)
                return
            }
            val tickChanceSectionKey = "tick-chance"
            val tickChanceSection = biteSection.getConfigurationSection(tickChanceSectionKey) ?: run {
                sendSectionErrorLog("$hookSectionKey.$biteSectionKey", tickChanceSectionKey)
                return
            }
            val biteChance = biteSection.getDouble("chance")
            val maxBite = biteSection.getInt("max")
            val fakePressPower = pressPowerSection.getDouble("fake")
            val realPressPower = pressPowerSection.getDouble("real")
            val pressPower = PressPower(fakePressPower, realPressPower)
            hookRegistry.setBiteChance(biteChance)
            hookRegistry.setMaxBite(maxBite)
            hookRegistry.setPressPower(pressPower)
            tickChanceSection.getKeys(false).forEach { tickText ->
                val tick = tickText.toLongOrNull() ?: run {
                    logger.warning("$hookSectionKey.$biteSectionKey.$tickChanceSectionKey 섹션에 ${tickText}은(는) 양의 정수가 아닙니다.")
                    return@forEach
                }
                val chance = tickChanceSection.getDouble(tickText)
                hookRegistry.setTick(tick, chance)
            }
        }
    }

    private fun loadMessageSection() {
        val messageSectionKey = "message"
        config.getConfigurationSection(messageSectionKey)?.apply {
            val getFishingAnnounce: (String) -> FishingAnnounce? = { sectionKey ->
                val fishingSound = getFishingSound()
                val announce = getString("announce")?.colorize() ?: ""
                val fishingChat = FishingChat(true, announce)
                if (fishingSound != null) {
                    FishingAnnounce(fishingSound, fishingChat)
                } else {
                    sendSectionErrorLog("$messageSectionKey.$sectionKey", "sound")
                    null
                }
            }
            val fakeBiteAnnounce = getFishingAnnounce("fake-bite") ?: return
            val realBiteAnnounce = getFishingAnnounce("real-bite") ?: return
            messageRegistry.set(AnnounceType.FAKE_BITE, fakeBiteAnnounce)
            messageRegistry.set(AnnounceType.REAL_BITE, realBiteAnnounce)
            val successfulSectionKey = "successful"
            getConfigurationSection(successfulSectionKey)?.apply {
                val shortSuccessfulSectionKey = "$messageSectionKey.$successfulSectionKey"
                val fishingSound = getFishingSound() ?: run {
                    sendSectionErrorLog(shortSuccessfulSectionKey, "sound")
                    return
                }
                val announceSectionKey = "announce"
                val announceSection = getConfigurationSection(announceSectionKey) ?: run {
                    sendSectionErrorLog(shortSuccessfulSectionKey, announceSectionKey)
                    return
                }
                val shortAnnounceSectionKey = "$shortSuccessfulSectionKey.$announceSectionKey"
                val chatSectionKey = "chat"
                val chatSection = announceSection.getConfigurationSection(chatSectionKey) ?: run {
                    sendSectionErrorLog(shortAnnounceSectionKey, chatSectionKey)
                    return
                }
                val fishingChat = chatSection.run {
                    val enabled = getBoolean("enabled")
                    val text = getString("text", "%fish%를 얻었다!")!!.colorize()
                    FishingChat(enabled, text)
                }
                val titleSectionKey = "chat"
                val titleSection = announceSection.getConfigurationSection(titleSectionKey) ?: run {
                    sendSectionErrorLog(shortAnnounceSectionKey, titleSectionKey)
                    return
                }
                val fishingTitle = titleSection.run {
                    val enabled = getBoolean("enabled")
                    val title = getString("title", "&c&l[ ! ]")!!.colorize()
                    val subTitle = getString("sub-title", "%fish%를 얻었다!")!!.colorize()
                    val fadeIn = getInt("fade-in")
                    val duration = getInt("duration")
                    val fadeOut = getInt("fade-out")
                    FishingTitle(enabled, title, subTitle, fadeIn, duration, fadeOut)
                }
                val fishingAnnounce = FishingAnnounce(fishingSound, fishingChat, fishingTitle)
                messageRegistry.set(AnnounceType.SUCCESSFUL, fishingAnnounce)
            }
        }
    }

    private fun ConfigurationSection.getFishingSound(): FishingSound? {
        return getConfigurationSection("sound")?.run {
            val enabled = getBoolean("enabled")
            val sound = getString("name", "minecraft:entity.generic.eat")!!
            val volume = getDouble("volume").toFloat()
            val pitch = getDouble("pitch").toFloat()
            FishingSound(enabled, sound, volume, pitch)
        }
    }

    private fun sendSectionErrorLog(targetSectionKey: String, emptySectionKey: String) {
        logger.warning("$targetSectionKey 섹션에 $emptySectionKey 섹션이 존재하지 않습니다.")
    }

    fun reload() {
        plugin.reloadConfig()
        hookRegistry.clear()
        load()
    }
}
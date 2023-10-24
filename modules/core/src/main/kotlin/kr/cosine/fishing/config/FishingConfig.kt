package kr.cosine.fishing.config

import kr.cosine.fishing.announce.FishingAnnounce
import kr.cosine.fishing.announce.FishingChat
import kr.cosine.fishing.announce.FishingSound
import kr.cosine.fishing.announce.FishingTitle
import kr.cosine.fishing.data.PressPower
import kr.cosine.fishing.enums.AnnounceType
import kr.cosine.fishing.registry.HookRegistry
import kr.cosine.fishing.registry.AnnounceRegistry
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration
import kr.hqservice.framework.yaml.config.HQYamlConfigurationSection
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

@Bean
class FishingConfig(
    private val plugin: Plugin,
    private val logger: Logger,
    private val config: HQYamlConfiguration,
    private val hookRegistry: HookRegistry,
    private val announceRegistry: AnnounceRegistry
) {

    fun load() {
        loadHookSection()
        loadMessageSection()
    }

    private fun loadHookSection() {
        val hookSectionKey = "hook"
        config.getSection(hookSectionKey)?.apply {
            val waitTimeSectionKey = "wait-time"
            val waitTimeSection = getSection(waitTimeSectionKey) ?: run {
                sendSectionErrorLog(hookSectionKey, waitTimeSectionKey)
                return
            }
            val minWaitTime = waitTimeSection.getInt("min")
            val maxWaitTime = waitTimeSection.getInt("max")
            hookRegistry.setWaitTime(minWaitTime, maxWaitTime)
            val biteSectionKey = "bite"
            val biteSection = getSection(biteSectionKey) ?: run {
                sendSectionErrorLog(hookSectionKey, biteSectionKey)
                return
            }
            val pressPowerSectionKey = "press-power"
            val pressPowerSection = biteSection.getSection(pressPowerSectionKey) ?: run {
                sendSectionErrorLog("$hookSectionKey.$biteSectionKey", pressPowerSectionKey)
                return
            }
            val tickChanceSectionKey = "tick-chance"
            val tickChanceSection = biteSection.getSection(tickChanceSectionKey) ?: run {
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
            tickChanceSection.getKeys().forEach { tickText ->
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
        val rootAnnounceSectionKey = "announce"
        config.getSection(rootAnnounceSectionKey)?.apply {
            val getFishingAnnounce: (String) -> FishingAnnounce? = { sectionKey ->
                val section = getSection(sectionKey)!!
                val fishingSound = section.getFishingSound()
                val announce = section.getString("announce").colorize()
                val fishingChat = FishingChat(true, announce)
                if (fishingSound != null) {
                    FishingAnnounce(fishingSound, fishingChat)
                } else {
                    sendSectionErrorLog("$rootAnnounceSectionKey.$sectionKey", "sound")
                    null
                }
            }
            val fakeBiteAnnounce = getFishingAnnounce("fake-bite") ?: return
            val realBiteAnnounce = getFishingAnnounce("real-bite") ?: return
            announceRegistry.apply {
                set(AnnounceType.FAKE_BITE, fakeBiteAnnounce)
                set(AnnounceType.REAL_BITE, realBiteAnnounce)
            }
            val successfulSectionKey = "successful"
            getSection(successfulSectionKey)?.apply {
                val shortSuccessfulSectionKey = "$rootAnnounceSectionKey.$successfulSectionKey"
                val fishingSound = getFishingSound() ?: run {
                    sendSectionErrorLog(shortSuccessfulSectionKey, "sound")
                    return
                }
                val announceSectionKey = "announce"
                val announceSection = getSection(announceSectionKey) ?: run {
                    sendSectionErrorLog(shortSuccessfulSectionKey, announceSectionKey)
                    return
                }
                val shortAnnounceSectionKey = "$shortSuccessfulSectionKey.$announceSectionKey"
                val chatSectionKey = "chat"
                val chatSection = announceSection.getSection(chatSectionKey) ?: run {
                    sendSectionErrorLog(shortAnnounceSectionKey, chatSectionKey)
                    return
                }
                val fishingChat = chatSection.run {
                    val enabled = getBoolean("enabled")
                    val text = getString("text", "%fish%를 얻었다!").colorize()
                    FishingChat(enabled, text)
                }
                val titleSectionKey = "title"
                val titleSection = announceSection.getSection(titleSectionKey) ?: run {
                    sendSectionErrorLog(shortAnnounceSectionKey, titleSectionKey)
                    return
                }
                val fishingTitle = titleSection.run {
                    val enabled = getBoolean("enabled")
                    val title = getString("title", "&c&l[ ! ]").colorize()
                    val subTitle = getString("sub-title", "%fish%를 얻었다!").colorize()
                    val fadeIn = getInt("fade-in")
                    val duration = getInt("duration")
                    val fadeOut = getInt("fade-out")
                    FishingTitle(enabled, title, subTitle, fadeIn, duration, fadeOut)
                }
                val fishingAnnounce = FishingAnnounce(fishingSound, fishingChat, fishingTitle)
                announceRegistry.set(AnnounceType.SUCCESSFUL, fishingAnnounce)
            }
        }
    }

    private fun HQYamlConfigurationSection.getFishingSound(): FishingSound? {
        return getSection("sound")?.run {
            val enabled = getBoolean("enabled")
            val sound = getString("name", "minecraft:entity.generic.eat")
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
        announceRegistry.clear()
        load()
    }
}
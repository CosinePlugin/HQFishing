package kr.cosine.fishing.command

import kr.cosine.fishing.service.FishManagementService
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.entity.Player

@Command(label = "낚시관리", isOp = true)
class FishingAdminCommand(
    private val fishManagementService: FishManagementService
) {

    @CommandExecutor("등록", "해당 key에 물고기를 등록합니다.", priority = 1)
    fun setFish(player: Player, key: String) {
        fishManagementService.setFish(key, player.inventory.itemInMainHand)
        player.sendMessage("$key 설정")
    }

    @CommandExecutor("설정", "물고기 설정 창을 오픈합니다.", priority = 2)
    fun openFishSettingView(player: Player) {
        fishManagementService.openFishSettingView(player)
    }

    @CommandExecutor("테스트")
    fun test(player: Player) {
        player.sendMessage("시간: ${player.world.time}")
    }
}
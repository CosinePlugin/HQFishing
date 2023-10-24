package kr.cosine.fishing.command

import kr.cosine.fishing.command.argument.KeyArgument
import kr.cosine.fishing.service.FishManagementService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.entity.Player

@Command(label = "낚시관리", isOp = true)
class FishingAdminCommand(
    private val fishManagementService: FishManagementService
) {

    @CommandExecutor("등록", "해당 key에 물고기를 등록합니다.", priority = 1)
    fun registerFish(
        player: Player,
        @ArgumentLabel("key") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        val itemStack = player.inventory.itemInMainHand.clone()
        val isSuccess = fishManagementService.registerFish(key, itemStack) ?: run {
            player.sendMessage("§c이미 등록된 key입니다.")
            return
        }
        if (isSuccess) {
            player.sendMessage("§a§l$key§a(을)를 등록하였습니다.")
        } else {
            player.sendMessage("§c손에 아이템을 들어주세요.")
        }
    }

    @CommandExecutor("제거", "해당 key를 제거합니다.", priority = 2)
    fun deleteFish(
        player: Player,
        @ArgumentLabel("key") keyArgument: KeyArgument
    ) {
        val key = keyArgument.key
        if (fishManagementService.deleteFish(key)) {
            player.sendMessage("§a§l$key§a(을)를 제거하였습니다.")
        } else {
            player.sendMessage("§c존재하지 않는 key입니다.")
        }
    }

    @CommandExecutor("설정", "물고기 설정 창을 오픈합니다.", priority = 3)
    fun openFishSettingView(player: Player) {
        fishManagementService.openFishSettingView(player)
    }

    @CommandExecutor("저장", "물고기 설정을 모두 저장합니다.", priority = 4)
    fun save(player: Player) {
        fishManagementService.save()
        player.sendMessage("§a물고기 설정을 모두 저장하였습니다.")
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 5)
    fun reload(player: Player) {
        fishManagementService.reload()
        player.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}
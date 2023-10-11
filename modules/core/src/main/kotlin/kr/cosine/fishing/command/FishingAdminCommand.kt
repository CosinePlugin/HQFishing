package kr.cosine.fishing.command

import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.entity.Player

@Command(label = "낚시관리", isOp = true)
class FishingAdminCommand {

    @CommandExecutor("테스트")
    fun test(player: Player) {
        player.sendMessage("시간: ${player.world.time}")
    }
}
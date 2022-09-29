package me.tech.commands.admin

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import me.tech.Kanade
import me.tech.azusa.toId
import me.tech.mizuhara.MinifactoryAPI
import org.bson.types.ObjectId
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class LoadFactoryFromIdCommand(
    private val plugin: Kanade
) : SuspendingCommandExecutor {
    private val profileManager get() = plugin.profileManager
    private val factoryManager get() = plugin.factoryManager

    override suspend fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(sender !is Player || !sender.hasPermission("core.admin")) {
            return true
        }

        if(args.isEmpty()) {
            sender.sendMessage("where da id :(")
            return true
        }

        val fac = MinifactoryAPI.getFactory(ObjectId(args[0]).toId())
        sender.sendMessage(fac.data!!.toString())

        return true
    }
}
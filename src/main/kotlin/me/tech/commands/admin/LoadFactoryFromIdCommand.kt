package me.tech.commands.admin

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import me.tech.Kanade
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

        val profile = profileManager.profiles[sender.uniqueId]
            ?: return true

        val factory = factoryManager.factories[profile.activeFactory]
        if(factory == null) {
            sender.sendMessage("Invalid factory.")
            return true
        }

        if(args.isNotEmpty()) {
            if(args[0].equals("t", true)) {
                factory.plot.tickable = true
                sender.sendMessage("tickable true")
            } else {
                factory.plot.clearPlot()
//            factory.plot.buildings.clear()
//            factory.plot.imaginaryConveyors.clear()
                sender.sendMessage("done")
            }
            return true
        }

        factory.plot.allBuildings.forEach { (t, building) ->
            sender.sendMessage("Bounds = ${building.buildingId} // Pos = ${building.position}")
        }

//        sender.sendMessage(factory.boundingBox.toString())

        return true
    }
}
package me.tech.commands.admin

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import me.tech.Kanade
import me.tech.azusa.toId
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bson.types.ObjectId
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

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
            sender.sendMessage("Bounds = ${building.structureId} // Pos = ${building.position}")
        }

//        sender.sendMessage(factory.boundingBox.toString())

        return true
    }
}
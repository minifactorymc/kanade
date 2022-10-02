package me.tech.commands.admin

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLib
import me.tech.Kanade
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SaveNewStructureCommand(
    private val plugin: Kanade
) : CommandExecutor {
    private val structureSaver
        get() = StructureBlockLib.INSTANCE.saveStructure(plugin)

    private val dataFolder
        get() = plugin.dataFolder

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if(!sender.hasPermission("core.admin") || sender !is Player) {
            return true
        }

        if(args == null || args.size < 4) {
            sender.sendMessage("Usage: /savenewstructure <name> <x> <y> <z>")
            sender.sendMessage("Note: Structure must be facing east.")
            return true
        }

        structureSaver
            // Offset so we can be in the corner of the build and execute the command.
            .at(sender.location.toCenterLocation().add(1.0, 0.0, 1.0))
            .sizeX(args[1].toInt())
            .sizeY(args[2].toInt())
            .sizeZ(args[3].toInt())
            .saveToPath(dataFolder.toPath().resolve(args[0]))
            .onException { it.printStackTrace() }
            .onResult {
                sender.sendMessage("Saved ${args[0]} with bounds (${args[1]}, ${args[2]}, ${args[3]})")
            }

        return true
    }
}
package me.tech.commands.admin

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLib
import me.tech.Kanade
import me.tech.mm
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import kotlin.concurrent.thread

class PreGeneratePlotSetsCommand : SuspendingCommandExecutor {
    private val structureLoader
        get() = StructureBlockLib.INSTANCE.loadStructure(JavaPlugin.getPlugin(Kanade::class.java))

    private val dataFolder
        get() = JavaPlugin.getPlugin(Kanade::class.java).dataFolder

    override suspend fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if(!sender.hasPermission("core.admin")) {
            sender.sendMessage("nah")
            return true
        }

        if(args.isEmpty() || args.size < 3) {
            sender.sendMessage(mm.deserialize("<red>Usage: /pregenerateplotsets <world> <amount> <z_offset> <structure_path>."))
            return true
        }
        val world = Bukkit.getWorld(args[0]) ?: run {
            sender.sendMessage(mm.deserialize("<red>Invalid world."))
            return true
        }
        val zOffset = args[2].toDouble()
        val structurePath = args.getOrNull(3) ?: "structures/etc/plotset_island"

        sender.sendMessage(mm.deserialize("<green>Loading structure <yellow>$structurePath<green>."))

        val locations = mutableSetOf<Location>()
        repeat(args[1].toInt()) {
            val location = Location(world, 0.0, 100.0, it * zOffset)
            if(!location.isChunkLoaded) {
                world.loadChunk(location.chunk)
            }

            locations.add(location)
        }

        thread {
            locations.forEachIndexed { int, location ->
                structureLoader
                    .at(location)
                    .loadFromFile(File(dataFolder, structurePath))
                    .onResult {
                        println("Completed loading structure #${int}.")
                    }
                    .onException {
                        it.printStackTrace()
                    }
            }
        }

        return true
    }
}
package me.tech

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import me.tech.commands.admin.LoadFactoryFromIdCommand
import me.tech.commands.admin.PreGeneratePlotSetsCommand
import me.tech.factory.FactoryManagerImpl
import me.tech.listeners.PlayerConnectListener
import me.tech.listeners.PlayerJoinListener
import me.tech.listeners.PlayerQuitListener
import me.tech.profile.ProfileManagerImpl
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Kanade : SuspendingJavaPlugin() {
    lateinit var profileManager: ProfileManagerImpl
        private set

    lateinit var factoryManager: FactoryManagerImpl
        private set

    override suspend fun onEnableAsync() {
        profileManager = ProfileManagerImpl()
        factoryManager = FactoryManagerImpl()

        factoryManager.generatePlotSets()

        getCommand("world")?.setSuspendingExecutor(object : SuspendingCommandExecutor {
            override suspend fun onCommand(
                sender: CommandSender,
                command: Command,
                label: String,
                args: Array<out String>
            ): Boolean {
                if(sender !is Player) {
                    return true
                }

                factoryManager.plots.forEach {
                    sender.sendMessage("CENTER = ${it.center}")
                    sender.sendMessage("PLOTS = ${it.plots}")
                    sender.sendMessage("-----------")
                }

                if(args.isNotEmpty()) {
                    sender.teleport(Location(Bukkit.getWorld("plots")!!, 0.0, 101.0, 0.0))
                    sender.sendMessage("done.")
                }

                return true
            }

        })

        getCommand("loadfactoryfromid")?.setSuspendingExecutor(LoadFactoryFromIdCommand(this))
        getCommand("pregenerateplotsets")?.setSuspendingExecutor(PreGeneratePlotSetsCommand())

        listOf(
            PlayerJoinListener(profileManager, factoryManager),
            PlayerQuitListener(profileManager)
        ).forEach {
            server.pluginManager.registerSuspendingEvents(it, this)
        }

        listOf(
            PlayerConnectListener(profileManager)
        ).forEach {
            server.pluginManager.registerEvents(it, this)
        }
    }
}

val mm = MiniMessage
    .builder()
    .build()
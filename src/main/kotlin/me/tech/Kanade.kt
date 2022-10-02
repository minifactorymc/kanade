package me.tech

import com.github.shynixn.mccoroutine.bukkit.SuspendingCommandExecutor
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import com.github.shynixn.mccoroutine.bukkit.setSuspendingExecutor
import me.tech.anya.Translator
import me.tech.anya.getLanguageProviderRegistration
import me.tech.commands.admin.*
import me.tech.factory.FactoryManagerImpl
import me.tech.listeners.*
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.requests.profile.SaveProfileInfoRequest
import me.tech.profile.ProfileManagerImpl
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Kanade : SuspendingJavaPlugin() {
    lateinit var translator: Translator
        private set

    lateinit var profileManager: ProfileManagerImpl
        private set

    lateinit var factoryManager: FactoryManagerImpl
        private set

    override suspend fun onEnableAsync() {
        translator = Translator(
            mm,
            getLanguageProviderRegistration(server.servicesManager)
        )
        translator.loadMessages(this)

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

        getCommand("generatetestitem")?.setExecutor(GenerateTestItemCommand())
        getCommand("loadfactoryfromid")?.setSuspendingExecutor(LoadFactoryFromIdCommand(this))
        getCommand("pregenerateplotsets")?.setSuspendingExecutor(PreGeneratePlotSetsCommand())
        getCommand("savenewstructure")?.setExecutor(SaveNewStructureCommand(this))
        getCommand("senditem")?.setExecutor(SendItemCommand(this))

        listOf(
            PlayerJoinListener(profileManager, factoryManager),
            PlayerQuitListener(profileManager, factoryManager)
        ).forEach {
            server.pluginManager.registerSuspendingEvents(it, this)
        }

        listOf(
            PlayerConnectListener(profileManager),
            BlockPlaceListener(profileManager, factoryManager),
            ServerTickListener(factoryManager)
        ).forEach {
            server.pluginManager.registerEvents(it, this)
        }
    }

    override suspend fun onDisableAsync() {
        profileManager.profiles.forEach { (_, profile) ->
            run {
                MinifactoryAPI.saveProfileInformation(profile.uuid, SaveProfileInfoRequest(profile.activeFactory))
            }
        }

        factoryManager.factories.forEach { (_, factory) ->
            run {
                factoryManager.save(factory)
            }
        }
    }
}

val translator: Translator
    get() = JavaPlugin.getPlugin(Kanade::class.java).translator

val mm = MiniMessage
    .builder()
    .build()
package me.tech

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import com.github.shynixn.mccoroutine.bukkit.registerSuspendingEvents
import me.tech.factory.FactoryManagerImpl
import me.tech.listeners.PlayerConnectListener
import me.tech.profile.ProfileManagerImpl
import net.kyori.adventure.text.minimessage.MiniMessage

class Kanade : SuspendingJavaPlugin() {
    lateinit var profileManager: ProfileManagerImpl
        private set

    lateinit var factoryManager: FactoryManagerImpl
        private set

    override suspend fun onEnableAsync() {
        profileManager = ProfileManagerImpl()
        factoryManager = FactoryManagerImpl()

        factoryManager.generatePlotSets()

        listOf(
            PlayerConnectListener(profileManager)
        ).forEach {
            server.pluginManager.registerSuspendingEvents(it, this)
        }
    }
}

val mm = MiniMessage
    .builder()
    .build()
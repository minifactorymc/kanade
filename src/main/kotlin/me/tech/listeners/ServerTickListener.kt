package me.tech.listeners

import com.destroystokyo.paper.event.server.ServerTickStartEvent
import me.tech.factory.FactoryManagerImpl
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ServerTickListener(
    private val factoryManager: FactoryManagerImpl
) : Listener {
    private val factories
        get() = factoryManager.factories.values

    private var skippedTick = false

    @EventHandler
    fun onServerTick(ev: ServerTickStartEvent) {
        skippedTick = if(skippedTick) {
            for(factory in factories) {
                factory.tick()
            }
            false
        } else {
            true
        }
    }
}
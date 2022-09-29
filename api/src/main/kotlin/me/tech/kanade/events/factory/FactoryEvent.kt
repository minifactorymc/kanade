package me.tech.kanade.events.factory

import me.tech.kanade.factory.FactorySession
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

abstract class FactoryEvent(
    factory: FactorySession
) : Event() {
    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}
package me.tech.listeners

import me.tech.factory.FactoryManagerImpl
import me.tech.mm
import me.tech.profile.ProfileManagerImpl
import me.tech.utils.cardinalDirection
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlaceListener(
    private val profileManager: ProfileManagerImpl,
    private val factoryManager: FactoryManagerImpl
) : Listener {
    private val profiles
        get() = profileManager.profiles

    private val factories
        get() = factoryManager.factories

    @EventHandler
    fun onBlockPlace(ev: BlockPlaceEvent) {
        val profile = profiles[ev.player.uniqueId]
            ?: throw NullPointerException("profile is null.")

        val factory = factories[profile.activeFactory]
            ?: return

        val id = if(ev.block.type == Material.STONE) "cool_conveyor" else "epic_conveyor"

        val loaded = factoryManager.loadBuilding(
            factory,
            ev.block.location,
            ev.player.cardinalDirection,
            id
        )
        if(!loaded) {
            ev.player.sendMessage(mm.deserialize("<red>Building is either outside plot or overlaps another building."))
        } else {
            ev.player.sendMessage("built!")
        }
    }
}
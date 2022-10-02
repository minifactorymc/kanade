package me.tech.listeners

import me.tech.factory.FactoryManagerImpl
import me.tech.kanade.factory.building.StructureLoadResult
import me.tech.kanade.utils.buildingId
import me.tech.profile.ProfileManagerImpl
import me.tech.utils.cardinalDirection
import org.bukkit.Bukkit
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
        val player = ev.player

        val profile = profiles[player.uniqueId]
            ?: throw NullPointerException("profile is null.")

        val factory = factories[profile.activeFactory]
            ?: return

        val buildingId = ev.itemInHand.buildingId
            ?: return

        val result: StructureLoadResult = try {
            factoryManager.loadBuilding(
                factory,
                ev.block.location,
                player.cardinalDirection,
                buildingId
            )
        } catch(ex: RuntimeException) {
            ex.printStackTrace()
            StructureLoadResult.EXCEPTION
        }

        if(result.isSuccess()) {
            player.sendMessage("Loaded")
        } else {
            ev.isCancelled = true
            player.sendMessage("Failed for : ${result.name}")
        }
    }
}
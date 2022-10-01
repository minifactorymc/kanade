package me.tech.listeners.admin

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLib
import com.github.shynixn.structureblocklib.api.enumeration.StructureRestriction
import me.tech.Kanade
import me.tech.kanade.utils.buildingId
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import java.io.File
import java.util.*
import kotlin.math.abs

class BuildingWandListener(
    private val plugin: Kanade
) : Listener {
    private val structureSaver
        get() = StructureBlockLib.INSTANCE.saveStructure(plugin)

    private val dataFolder: File
        get() = plugin.dataFolder

    @EventHandler
    fun onSetPosition(ev: PlayerInteractEvent) {
        if(ev.action == Action.LEFT_CLICK_BLOCK && isHoldingBuildingWand(ev.player)) {
            val location = ev.clickedBlock?.location
                ?: return

            ev.isCancelled = true

            val name = System.currentTimeMillis().toString()

            File(dataFolder, "structures/buildings/cool_conveyor").delete()

            // BUILDING MUST BE FACING EAST
            structureSaver
                .at(location)
                .restriction(StructureRestriction.UNLIMITED)
                .sizeX(3)
                .sizeY(3)
                .sizeZ(3)
                .offSet(Vector(1.0, 0.0, 1.0))
                .saveToPath(dataFolder.toPath().resolve("structures/buildings/cool_conveyor"))
                .onResult {
                    ev.player.sendMessage("Saved as $name")
                }
        }
    }

    private fun isHoldingBuildingWand(player: Player): Boolean {
        if(!player.hasPermission("core.admin")) {
            return false
        }
        val id = player.inventory.itemInMainHand.buildingId
            ?: return false

        return id.equals("developer_building_wand", true)
    }
}
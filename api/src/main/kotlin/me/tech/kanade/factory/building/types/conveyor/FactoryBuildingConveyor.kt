package me.tech.kanade.factory.building.types.conveyor

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.item.FactoryBuildingItemHolder
import me.tech.kanade.factory.building.FactoryBuildingTickable
import me.tech.kanade.factory.building.item.FactoryPlotItem
import me.tech.kanade.utils.toLocation
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

abstract class FactoryBuildingConveyor(
    buildingId: String,
    position: Coordinates,
    facing: BlockFace,
    val ticksToMove: Int,
    override val allowHorizontalIntake: Boolean,
    override val allowVerticalIntake: Boolean,
    val nextYOffset: Double = 0.0
): FactoryBuilding(buildingId, position, facing), FactoryBuildingTickable, FactoryBuildingItemHolder {
    override var currentTick = 0

    override var carriedItem: FactoryPlotItem? = null

    fun moveItemToNext(
        from: FactoryBuildingConveyor,
        to: FactoryBuilding,
        item: FactoryPlotItem
    ): Boolean {
        if(to !is FactoryBuildingItemHolder) {
            return false
        }

        if(!validateChecks(from, to, item)) {
            return false
        }

        from.carriedItem = null
        to.carriedItem = item

        item.armorStand.teleport(
            to.position.toLocation(item.armorStand.world).toCenterLocation()
        )

        return true
    }

    /**
     * Implement extended functionality and checks here.
     */
    open fun validateChecks(from: FactoryBuildingConveyor, to: FactoryBuilding, item: FactoryPlotItem): Boolean {
        if(!validateBaseChecks(from, to, item)) {
            return false
        }

        return true
    }

    /**
     * All basic checks to run before allowing passage.
     */
    private fun validateBaseChecks(from: FactoryBuildingConveyor, to: FactoryBuilding, item: FactoryPlotItem): Boolean {
        // quickly cast it because it's true.
        (to as FactoryBuildingItemHolder)

        if(from.currentTick++ != from.ticksToMove) {
            return false
        }
        from.currentTick = 0

        if(!to.allowItemIntake) {
            return false
        }

        if(!to.allowVerticalIntake && from.position.y != to.position.y) {
            return false
        }

        if(!to.allowHorizontalIntake && from.position.y == to.position.y) {
            return false
        }

        return true
    }
}
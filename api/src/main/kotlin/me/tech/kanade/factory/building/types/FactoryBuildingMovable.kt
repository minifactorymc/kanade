package me.tech.kanade.factory.building.types

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.item.FactoryPlotItem
import me.tech.kanade.utils.toLocation

interface FactoryBuildingMovable {
    val ticksToMove: Int

    var currentTick: Int

    val canMoveToNext: Boolean
        get() {
            if(currentTick++ != ticksToMove) {
                return false
            }

            currentTick = 0
            return true
        }

    fun moveItemToNext(
        from: FactoryBuilding,
        to: FactoryBuilding,
        item: FactoryPlotItem
    ): Boolean {
        val stand = item.armorStand

        if(!to.allowItemIntake || !canMoveToNext) {
            return false
        }

        from.carriedItem = null
        to.carriedItem = item

        stand.teleport(
            to.position.toLocation(stand.world).toCenterLocation()
        )
        return true
    }
}
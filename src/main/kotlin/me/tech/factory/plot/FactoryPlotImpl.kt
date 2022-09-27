package me.tech.factory.plot

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.mizuhara.models.Coordinates
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Location
import org.litote.kmongo.Id

class FactoryPlotImpl(
    var occupant: Id<FactoryDocument>?,
    val position: FactoryPlotPosition,
    val owningPlotSet: PlotSet
) {
    val center by lazyOf(Location(
        owningPlotSet.center.world,
        owningPlotSet.center.x + position.offset.x,
        owningPlotSet.center.y + position.offset.y,
        owningPlotSet.center.z + position.offset.z
    ))

    val buildings = mutableMapOf<Coordinates, FactoryBuilding>()

    val taken: Boolean
        get() = occupant != null

    var tickable = false
}

fun generateUnoccupiedPlots(
    owningPlotSet: PlotSet
): List<FactoryPlotImpl> {
    return FactoryPlotPosition.values()
        .map { FactoryPlotImpl(null, it, owningPlotSet) }
}
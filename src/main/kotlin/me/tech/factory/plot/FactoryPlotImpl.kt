package me.tech.factory.plot

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.plot.FactoryPlot
import me.tech.kanade.factory.plot.FactoryPlotPosition
import me.tech.mizuhara.models.Coordinates
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import org.litote.kmongo.Id

class FactoryPlotImpl(
    override var occupant: Id<FactoryDocument>?,
    override val position: FactoryPlotPosition,
    val owningPlotSet: PlotSet
): FactoryPlot {
    val center by lazyOf(Location(
        owningPlotSet.center.world,
        owningPlotSet.center.x + position.offset.x,
        owningPlotSet.center.y + position.offset.y,
        owningPlotSet.center.z + position.offset.z
    ))

    override val boundingBox = BoundingBox.of(center, position.offset.x, 10.0, position.offset.z)

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
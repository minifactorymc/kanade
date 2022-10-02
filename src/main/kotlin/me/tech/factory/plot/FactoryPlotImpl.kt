package me.tech.factory.plot

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.plot.FactoryPlot
import me.tech.kanade.factory.plot.FactoryPlotPosition
import me.tech.mizuhara.models.Coordinates
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.util.BoundingBox
import org.litote.kmongo.Id


class FactoryPlotImpl(
    override var occupant: Id<FactoryDocument>?,
    override val position: FactoryPlotPosition,
    val owningPlotSet: PlotSet
): FactoryPlot {
    companion object {
        private const val BOUNDS_X = 12.5
        private const val BOUNDS_Y = 10.0
        private const val BOUNDS_Z = 12.5
    }

    private val _center by lazyOf(Location(
        owningPlotSet.center.world,
        owningPlotSet.center.x + position.offset.x,
        owningPlotSet.center.y + position.offset.y,
        owningPlotSet.center.z + position.offset.z
    ))
    override val center: Location
        get() = _center.clone()

    override val boundingBox = BoundingBox.of(
        center,
        BOUNDS_X, BOUNDS_Y, BOUNDS_Z
    )

    val buildings = mutableMapOf<Coordinates, FactoryBuilding>()

    val taken: Boolean
        get() = occupant != null

    var tickable = false

    fun clearPlot() {
        tickable = false

        // TODO: 10/1/2022 clear the plot, maybe use a void structure?

        buildings.clear()
    }
}

fun generateUnoccupiedPlots(
    owningPlotSet: PlotSet
): List<FactoryPlotImpl> {
    return FactoryPlotPosition.values()
        .map { FactoryPlotImpl(null, it, owningPlotSet) }
}
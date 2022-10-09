package me.tech.factory.plot

import me.tech.factory.plot.buildings.ImaginaryConveyor
import me.tech.factory.plot.buildings.getImaginaryConveyorLocations
import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.FactoryBuildingTickable
import me.tech.kanade.factory.building.types.conveyor.FactoryBuildingConveyor
import me.tech.kanade.factory.plot.FactoryPlot
import me.tech.kanade.factory.plot.FactoryPlotPosition
import me.tech.kanade.utils.toCoordinates
import me.tech.kanade.utils.toLocation
import me.tech.mizuhara.models.Coordinates
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import org.litote.kmongo.Id
import java.util.*


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

    private val plotsWorld get() = center.world

    override val boundingBox = BoundingBox.of(
        center,
        BOUNDS_X, BOUNDS_Y, BOUNDS_Z
    )

    // TODO: 10/2/2022 I converted the private fields to sets and just turn them
    // into maps, need to still test the performance of this.

    private val _buildings = mutableMapOf<Coordinates, FactoryBuilding>()
    val buildings: Map<Coordinates, FactoryBuilding>
        get() = Collections.unmodifiableMap(_buildings)

    private val _imaginaryConveyors = mutableMapOf<Coordinates, ImaginaryConveyor>()
    val imaginaryConveyors: Map<Coordinates, ImaginaryConveyor>
        get() = Collections.unmodifiableMap(_imaginaryConveyors)

    val allBuildings: Map<Coordinates, FactoryBuilding>
        get() = buildings + imaginaryConveyors

    val taken: Boolean
        get() = occupant != null

    var tickable = false

    fun addBuildingInstance(building: FactoryBuilding) {
        _buildings[building.position] = building

        if(building is FactoryBuildingConveyor) {
            setupImaginaryConveyors(building)
        }
    }

    fun removeBuildingInstance(coordinates: Coordinates) {
        removeBuildingInstance(buildings[coordinates] ?: return)
    }

    fun removeBuildingInstance(building: FactoryBuilding) {
        if(building is FactoryBuildingConveyor) {
            removeImaginaryConveyors(building)
        }
        _buildings.values.removeIf { it == building }
    }

    private fun setupImaginaryConveyors(building: FactoryBuildingConveyor) {
        val bounds = building.structureBounds
        val location = building.position.toLocation(plotsWorld)
        val facing = building.facing
        val ticksToMove = building.ticksToMove

        getImaginaryConveyorLocations(bounds, location, facing, building.nextYOffset != 0.0).forEach {
            _imaginaryConveyors[it.toCoordinates()] = ImaginaryConveyor(
                it.toCoordinates(),
                facing,
                ticksToMove,
                building.allowHorizontalIntake,
                building.allowVerticalIntake,
                building.nextYOffset
            )
        }
    }

    private fun removeImaginaryConveyors(building: FactoryBuildingConveyor) {
        val bounds = building.structureBounds
        val location = building.position.toLocation(plotsWorld)
        val facing = building.facing

        getImaginaryConveyorLocations(bounds, location, facing, building.nextYOffset != 0.0).forEach {
            _imaginaryConveyors.remove(it.toCoordinates())
        }
    }

    override fun tickBuildings() {
        if(!tickable) {
            return
        }

        for(building in allBuildings.values) {
            if(building !is FactoryBuildingTickable) {
                continue
            }

            if(building is FactoryBuildingConveyor) {
                val item = building.carriedItem
                if(item != null) {
                    val nextPosition = building.nextConnection.copy()
                    nextPosition.y += building.nextYOffset

                    val nextBuilding = allBuildings[nextPosition]

                    if(nextBuilding != null) {
                        building.moveItemToNext(building, nextBuilding, item)
                    }
                }
            }
        }
    }

    fun clearPlot() {
        tickable = false

        // TODO: 10/1/2022 clear the plot, maybe use a void structure?

        _buildings.clear()
        _imaginaryConveyors.clear()
    }
}

fun generateUnoccupiedPlots(
    owningPlotSet: PlotSet
): List<FactoryPlotImpl> {
    return FactoryPlotPosition.values()
        .map { FactoryPlotImpl(null, it, owningPlotSet) }
}
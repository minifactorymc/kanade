package me.tech.factory.plot

import me.tech.factory.FactoryImpl
import me.tech.factory.plot.buildings.createBuildingInstance
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.Coordinates
import me.tech.mizuhara.models.mongo.factory.plot.FactoryPlotBuildingDocument
import me.tech.mizuhara.models.mongo.factory.plot.FactoryPlotDocument
import me.tech.mizuhara.models.requests.factory.plot.SavePlotRequest
import me.tech.utils.toCoordinates
import me.tech.utils.toLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import java.util.*

class FactoryPlotManager {
    companion object {
        private const val PLOT_SETS = 2
        private const val Z_OFFSET = 250.0

        // TODO: 9/27/2022 SWITCH TO UID - FIXED
        private val PLOTS_WORLD by lazyOf(Bukkit.getWorld("plots")!!)

        private val PLOT_SET_CENTER_OFFSET = Coordinates(10.0, 0.0, 10.0)
    }

    private val _plotSets = mutableSetOf<PlotSet>()
    val plotSets: Set<PlotSet>
        get() = Collections.unmodifiableSet(_plotSets)

    fun load(document: FactoryPlotDocument) {
        // TODO: 9/27/2022 throw error do smth idk
        val plot = plotSets.findSuitablePlot()
            ?: return

        loadBuildings(plot, document.buildings)
    }

    suspend fun saveAndUnload(factory: FactoryImpl) {
        val plot = factory.plot
        plot.occupant = null
        plot.tickable = false

        val buildings = factory.plot.buildings.map {
            FactoryPlotBuildingDocument(
                it.value.structureId,
                it.key.toLocation(PLOTS_WORLD).clone().subtract(factory.plot.owningPlotSet.center).toCoordinates(),
                it.value.facing.name.uppercase()
            )
        }

        MinifactoryAPI.saveFactoryPlot(SavePlotRequest(
            factory.id,
            buildings
        ))
    }

    private fun loadBuildings(
        plot: FactoryPlotImpl,
        buildings: List<FactoryPlotBuildingDocument>
    ) {
        for(building in buildings) {
            val structureId = building.structureId
            val location = plot.center
                .clone()
                .add(building.offset.toLocation(PLOTS_WORLD))
            val facing = BlockFace.valueOf(building.facing)

            // TODO: 9/27/2022 load structures

            // TODO: 9/27/2022 throw error.
            val buildingInst = createBuildingInstance(structureId, location, facing)
                ?: continue

            plot.buildings[location.toCoordinates()] = buildingInst
        }

        plot.tickable = true
    }

    fun generatePlotSets() {
        repeat(PLOT_SETS) {
            val location = Location(PLOTS_WORLD, 0.0, 100.0, plotSets.size * Z_OFFSET).also {
                if(it.isChunkLoaded) {
                    return@also
                }
                PLOTS_WORLD.loadChunk(it.chunk)
            }

            val plotSet = PlotSet(
                Coordinates(0.0, 0.0, 0.0),
                location.clone().add(PLOT_SET_CENTER_OFFSET.toLocation(PLOTS_WORLD))
            )
            plotSet.plots = generateUnoccupiedPlots(plotSet)

            _plotSets.add(plotSet)
        }
    }
}
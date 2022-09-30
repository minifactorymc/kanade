package me.tech.factory.plot

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLib
import com.github.shynixn.structureblocklib.api.enumeration.StructureRotation
import me.tech.Kanade
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
import org.bukkit.WorldCreator
import org.bukkit.block.BlockFace
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*
import kotlin.concurrent.thread

class FactoryPlotManager {
    companion object {
        private const val PLOT_SETS = 3
        private const val Z_OFFSET = 250.0

        // TODO: 9/27/2022 SWITCH TO UID - FIXED
        private val PLOTS_WORLD by lazyOf(
            Bukkit.createWorld(WorldCreator.name("plots"))
                ?: throw NullPointerException("plots not found")
        )

        private val PLOT_SET_CENTER_OFFSET = Coordinates(30.0, 0.0, 30.0)
    }

    private val _plotSets = mutableSetOf<PlotSet>()
    val plotSets: Set<PlotSet>
        get() = Collections.unmodifiableSet(_plotSets)

    private val structureLoader
        get() = StructureBlockLib.INSTANCE.loadStructure(JavaPlugin.getPlugin(Kanade::class.java))

    private val dataFolder
        get() = JavaPlugin.getPlugin(Kanade::class.java).dataFolder

    fun load(
        document: FactoryPlotDocument
    ): FactoryPlotImpl? {
        // TODO: 9/27/2022 throw error do smth idk
        val plot = plotSets.findSuitablePlot()
            ?: return null

        loadBuildings(plot, document.buildings).also {
            plot.tickable = true
        }

        return plot
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

        MinifactoryAPI.saveFactoryPlot(factory.id, SavePlotRequest(
            buildings
        ))
    }

    private fun loadBuildings(
        plot: FactoryPlotImpl,
        buildings: List<FactoryPlotBuildingDocument>
    ) {
        if(buildings.isEmpty()) {
            return
        }

        data class PendingBuilding(
            val location: Location,
            val facing: BlockFace,
            val structureId: String
        )

        val pending = mutableListOf<PendingBuilding>()
        for(building in buildings) {
            val structureId = building.structureId
            val location = plot.center
                .clone()
                .add(building.offset.toLocation(PLOTS_WORLD))
            val facing = BlockFace.valueOf(building.facing)

            pending.add(PendingBuilding(
                location,
                facing,
                structureId
            ))

            // TODO: 9/27/2022 throw error. - Update: I forgot what this comment was about lol
            val buildingInst = createBuildingInstance(structureId, location, facing)
                ?: continue

            plot.buildings[location.toCoordinates()] = buildingInst
        }

        thread {
            pending.forEach {
                val (location, facing, structureId) = it
                val rotation = when(facing) {
                    BlockFace.NORTH -> StructureRotation.NONE
                    BlockFace.EAST -> StructureRotation.ROTATION_90
                    BlockFace.SOUTH -> StructureRotation.ROTATION_180
                    BlockFace.WEST -> StructureRotation.ROTATION_270
                    else -> StructureRotation.NONE
                }

                structureLoader
                    .at(location.subtract(0.0, 1.0, 0.0))
                    .rotation(rotation)
                    // TODO: 9/29/2022 Cache the files so we dont need to keep creating an inst.
                    .loadFromFile(File(dataFolder, "structures/buildings/$structureId"))
                    .onException { throwable ->
                        throwable.printStackTrace()
                    }
            }
        }
    }

    fun generatePlotSets() {
        repeat(PLOT_SETS) {
            val location = Location(PLOTS_WORLD, 0.0, 100.0, plotSets.size * Z_OFFSET).also {
                if(!it.isChunkLoaded) {
                    PLOTS_WORLD.loadChunk(it.chunk)
                }
            }

            val plotSet = PlotSet(
                Coordinates(0.0, 0.0, 0.0),
                location.clone().add(PLOT_SET_CENTER_OFFSET.toLocation(PLOTS_WORLD)).toCenterLocation()
            )
            plotSet.plots = generateUnoccupiedPlots(plotSet)

            _plotSets.add(plotSet)
        }
    }
}
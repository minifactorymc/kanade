package me.tech.factory

import me.tech.factory.plot.FactoryPlotManager
import me.tech.factory.plot.buildings.createBuildingInstance
import me.tech.kanade.factory.building.structure.FactoryBuildingStructure
import me.tech.kanade.factory.building.structure.StructureLoadResult
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.litote.kmongo.Id

class FactoryManagerImpl {
    private val plotManager = FactoryPlotManager()

    private val _factories = mutableMapOf<Id<FactoryDocument>, FactoryImpl>()
    val factories: Map<Id<FactoryDocument>, FactoryImpl>
        get() = _factories

    // TODO: 9/28/2022 remove?
    val plots get() = plotManager.plotSets

    fun add(factory: FactoryImpl) {
        _factories[factory.id] = factory
    }

    fun remove(id: Id<FactoryDocument>) {
        _factories.remove(id)
    }

    fun load(document: FactoryDocument): FactoryImpl {
        val plot = plotManager.load(document.plot)
            ?: throw NullPointerException("couldn't find valid plot")

        val factory = document.toFactory(plot)

        add(factory)
        return factory
    }

    fun unload(id: Id<FactoryDocument>) {
    }

    suspend fun save(factory: FactoryImpl) {
        plotManager.saveAndUnload(factory)
    }

    fun loadBuilding(
        factory: FactoryImpl,
        location: Location,
        facing: BlockFace,
        buildingId: String
    ): StructureLoadResult {
        val plot = factory.plot
        Bukkit.broadcastMessage("building id = $buildingId")
        val result = plotManager.loadStructure(
            plot,
            location,
            facing,
            FactoryBuildingStructure.valueOf(buildingId.uppercase())
        )

        if(result.isSuccess()) {
            val inst = createBuildingInstance(buildingId, location, facing)
                ?: throw RuntimeException("unable to create instance of building $buildingId.")

            plot.addBuildingInstance(inst)
        }
        return result
    }

    fun generatePlotSets() {
        plotManager.generatePlotSets()
    }
}
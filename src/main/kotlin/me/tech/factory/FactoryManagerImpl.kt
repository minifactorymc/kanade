package me.tech.factory

import me.tech.factory.plot.FactoryPlotManager
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import me.tech.mizuhara.models.requests.factory.plot.SavePlotRequest
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

    fun load(document: FactoryDocument): FactoryImpl {
        val plot = plotManager.load(document.plot)
            ?: throw NullPointerException("couldn't find valid plot")

        return document.toFactory(plot)
    }

    fun unload(id: Id<FactoryDocument>) {
    }

    suspend fun save(factory: FactoryImpl) {
        plotManager.saveAndUnload(factory)
    }

    fun generatePlotSets() {
        plotManager.generatePlotSets()
    }
}
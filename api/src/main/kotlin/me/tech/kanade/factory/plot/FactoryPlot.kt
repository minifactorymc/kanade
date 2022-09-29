package me.tech.kanade.factory.plot

import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.litote.kmongo.Id

interface FactoryPlot {
    val occupant: Id<FactoryDocument>?

    val position: FactoryPlotPosition
}
package me.tech.kanade.factory.plot

import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Location
import org.bukkit.util.BoundingBox
import org.litote.kmongo.Id

interface FactoryPlot {
    val occupant: Id<FactoryDocument>?

    val position: FactoryPlotPosition

    val boundingBox: BoundingBox

    val center: Location
}
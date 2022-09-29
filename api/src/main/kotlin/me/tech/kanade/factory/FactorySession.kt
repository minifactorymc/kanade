package me.tech.kanade.factory

import me.tech.kanade.factory.plot.FactoryPlot
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.litote.kmongo.Id
import java.util.*

interface FactorySession {
    val id: Id<FactoryDocument>

    val founder: UUID

    val team: List<FactoryProfile>

    val plot: FactoryPlot
}
package me.tech.factory

import me.tech.factory.plot.FactoryPlotImpl
import me.tech.kanade.factory.FactorySession
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.litote.kmongo.Id
import java.util.UUID

class FactoryImpl(
    override val id: Id<FactoryDocument>,
    override val founder: UUID,
    override val team: List<FactoryProfileImpl>,
    override val plot: FactoryPlotImpl
): FactorySession {
}

fun FactoryDocument.toFactory(plot: FactoryPlotImpl): FactoryImpl {
    return FactoryImpl(
        this.id,
        this.founder,
        this.team.map { it.toFactoryProfile() },
        plot
    )
}
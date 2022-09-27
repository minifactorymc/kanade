package me.tech.factory

import me.tech.factory.plot.FactoryPlotImpl
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.litote.kmongo.Id
import java.util.UUID

class FactoryImpl(
    val id: Id<FactoryDocument>,
    val founder: UUID,
    val team: List<FactoryProfileImpl>
) {
    lateinit var plot: FactoryPlotImpl
        private set
}

fun FactoryDocument.toFactory(): FactoryImpl {
    return FactoryImpl(
        this.id,
        this.founder,
        this.team.map { it.toFactoryProfile() }
    )
}
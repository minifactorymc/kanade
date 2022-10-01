package me.tech.factory

import me.tech.factory.plot.FactoryPlotImpl
import me.tech.kanade.factory.FactorySession
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import org.litote.kmongo.Id
import java.util.UUID

class FactoryImpl(
    override val id: Id<FactoryDocument>,
    override val founder: UUID,
    override val team: List<FactoryProfileImpl>,
    override val plot: FactoryPlotImpl
): FactorySession {
    val boundingBox: BoundingBox
        get() = plot.boundingBox

    // TODO: 9/30/2022 check if plot is open etc, this method will be used
    // by everything to validate whether someone can be brought to a plot then
    // actually teleport them.
    fun teleportToPlot(player: Player): Boolean {
        player.teleport(plot.center.add(0.0, 1.0, 0.0))
        return true
    }
}

fun FactoryDocument.toFactory(plot: FactoryPlotImpl): FactoryImpl {
    return FactoryImpl(
        this.id,
        this.founder,
        this.team.map { it.toFactoryProfile() },
        plot
    )
}
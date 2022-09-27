package me.tech.factory.plot

import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location

data class PlotSet(
    val offset: Coordinates,
    val center: Location
) {
    lateinit var plots: List<FactoryPlotImpl>

    // TODO: 9/27/2022 make.
    val capacity: Capacity
        get() {
            return Capacity.FULL
        }

    enum class Capacity {
        FULL,
        PARTIAL,
        EMPTY
    }
}

fun Set<PlotSet>.findSuitablePlot(): FactoryPlotImpl? {
    // Match with partially full plots then move to empty ones.
    val plotSet = filter { it.capacity != PlotSetCapacity.FULL }
        .firstOrNull { it.capacity == PlotSetCapacity.PARTIAL }
        ?: firstOrNull { it.capacity == PlotSetCapacity.EMPTY }
        ?: return null

    return plotSet.plots.first { !it.taken }
}

typealias PlotSetCapacity = PlotSet.Capacity
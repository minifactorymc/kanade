package me.tech.kanade.factory.plot

import me.tech.mizuhara.models.Coordinates

enum class FactoryPlotPosition(
    private val x: Double,
    private val z: Double
) {
    // PlotSet faces north.
    TOP_LEFT(-16.0, -16.0),
    TOP_RIGHT(16.0, -16.0),

    BOTTOM_LEFT(-16.0, 16.0),
    BOTTOM_RIGHT(16.0, 16.0);

    val offset: Coordinates
        get() = Coordinates(x, 0.0, z)
}
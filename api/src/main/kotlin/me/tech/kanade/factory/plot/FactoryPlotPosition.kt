package me.tech.kanade.factory.plot

import me.tech.mizuhara.models.Coordinates

enum class FactoryPlotPosition(
    private val x: Double,
    private val z: Double
) {
    // PlotSet faces east.
    TOP_LEFT(16.0, -16.0),
    TOP_RIGHT(16.0, 16.0),

    BOTTOM_LEFT(-16.0, -16.0),
    BOTTOM_RIGHT(-16.0, 16.0);

    val offset: Coordinates
        // +1.0 Y to get in position with the center block.
        get() = Coordinates(x, 1.5, z)
}
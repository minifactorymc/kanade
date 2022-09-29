package me.tech.kanade.events.factory

import me.tech.kanade.factory.FactorySession
import me.tech.kanade.factory.plot.FactoryPlot

class FactoryPlotLoadedEvent(
    factory: FactorySession,
    val plot: FactoryPlot
): FactoryEvent(factory) {
}
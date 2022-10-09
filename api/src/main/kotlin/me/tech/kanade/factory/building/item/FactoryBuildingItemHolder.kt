package me.tech.kanade.factory.building.item

import me.tech.kanade.factory.building.item.FactoryPlotItem

interface FactoryBuildingItemHolder {
    var carriedItem: FactoryPlotItem?

    val allowItemIntake: Boolean
        get() = carriedItem == null

    val allowVerticalIntake: Boolean

    val allowHorizontalIntake: Boolean
}
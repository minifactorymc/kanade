package me.tech.kanade.factory.building

import me.tech.kanade.factory.building.item.FactoryPlotItem
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox

abstract class FactoryBuilding(
    val structureId: String,
    val position: Coordinates,
    val connections: FactoryBuildingConnections,
    val facing: BlockFace
) {
    val structure: FactoryBuildingStructure
        get() = FactoryBuildingStructure.valueOf(structureId.uppercase())

    val structureBounds: FactoryBuildingStructure.Bounds
        get() = structure.bounds

    lateinit var boundingBox: BoundingBox

    var carriedItem: FactoryPlotItem? = null

    val allowItemIntake: Boolean
        get() = carriedItem == null
}
package me.tech.kanade.factory.building

import me.tech.kanade.factory.building.structure.FactoryBuildingStructure
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox

abstract class FactoryBuilding(
    val buildingId: String,
    val position: Coordinates,
    val facing: BlockFace
) {
    val nextConnection = Coordinates(
        position.x.plus(facing.modX),
        position.y,
        position.z.plus(facing.modZ)
    )

    val structure: FactoryBuildingStructure
        get() = FactoryBuildingStructure.valueOf(buildingId.uppercase())

    val structureBounds: FactoryBuildingStructure.NewBounds
        get() = structure.bounds

    lateinit var boundingBox: BoundingBox
}
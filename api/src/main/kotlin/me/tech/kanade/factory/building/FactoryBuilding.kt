package me.tech.kanade.factory.building

import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

abstract class FactoryBuilding(
    val structureId: String,
    val position: Coordinates,
    val connections: FactoryBuildingConnections,
    val facing: BlockFace
) {

}
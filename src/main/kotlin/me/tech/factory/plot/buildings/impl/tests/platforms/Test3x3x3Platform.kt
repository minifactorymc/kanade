package me.tech.factory.plot.buildings.impl.tests.platforms

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

class Test3x3x3Platform(
    buildingId: String,
    position: Coordinates,
    facing: BlockFace,
): FactoryBuilding(
   buildingId, position, facing
) {
}
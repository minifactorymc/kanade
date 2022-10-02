package me.tech.factory.plot.buildings

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.FactoryBuildingConnections
import me.tech.kanade.factory.building.types.FactoryBuildingMovable
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

class ImaginaryConveyor(
    position: Coordinates,
    connections: FactoryBuildingConnections,
    facing: BlockFace,
    override val ticksToMove: Int
) : FactoryBuilding(
    "imaginary",
    position, connections, facing
), FactoryBuildingMovable {
    override var currentTick: Int = 0
}
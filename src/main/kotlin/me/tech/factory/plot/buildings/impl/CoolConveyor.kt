package me.tech.factory.plot.buildings.impl

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.FactoryBuildingConnections
import me.tech.kanade.factory.building.types.FactoryBuildingMovable
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

class CoolConveyor(
    position: Coordinates,
    connections: FactoryBuildingConnections,
    facing: BlockFace
) : FactoryBuilding(
    "cool_conveyor",
    position, connections, facing
), FactoryBuildingMovable {
    override val ticksToMove = 8

    override var currentTick = 0
}
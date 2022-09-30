package me.tech.factory.plot.buildings

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.FactoryBuildingConnections
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

class EpicConveyor(
    position: Coordinates,
    connections: FactoryBuildingConnections,
    facing: BlockFace
) : FactoryBuilding(
    "epic_conveyor",
    position, connections, facing
) {
}
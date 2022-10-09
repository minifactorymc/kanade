package me.tech.factory.plot.buildings.impl.tests.vertical

import me.tech.kanade.factory.building.types.conveyor.FactoryBuildingConveyor
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

class TestVertical1x1Conveyor(
    buildingId: String,
    position: Coordinates,
    facing: BlockFace,
): FactoryBuildingConveyor(
    buildingId, position, facing,
    10, true, true, 1.0
)
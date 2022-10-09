package me.tech.factory.plot.buildings.impl.tests.horizontal

import me.tech.kanade.factory.building.types.conveyor.FactoryBuildingConveyor
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

class TestHorizontal3x3Conveyor(
    buildingId: String,
    position: Coordinates,
    facing: BlockFace,
): FactoryBuildingConveyor(
    buildingId, position, facing,
    5,true, true, 0.0
)
package me.tech.factory.plot.buildings

import me.tech.kanade.factory.building.types.conveyor.FactoryBuildingConveyor
import me.tech.mizuhara.models.Coordinates
import org.bukkit.block.BlockFace

/**
 * Represents a non-existent conveyor belt which is only used
 * to move items along by creating a fake conveyor belt.
 * @internal Please do not use this under any circumstance.
 */
class ImaginaryConveyor(
    position: Coordinates,
    facing: BlockFace,
    ticksToMove: Int,
    override val allowHorizontalIntake: Boolean,
    override val allowVerticalIntake: Boolean,
    nextYOffset: Double
): FactoryBuildingConveyor(
    "imaginary", position, facing, ticksToMove, allowHorizontalIntake, allowVerticalIntake, nextYOffset
)
package me.tech.kanade.factory.building

import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.block.BlockFace

data class FactoryBuildingConnections(
    val next: Coordinates,
    val previous: Coordinates
) {
    companion object {
        fun fromLocation(
            location: Location,
            facing: BlockFace
        ): FactoryBuildingConnections {
            // TODO: 9/27/2022 handle 3x3 / 5x5 structures
            return FactoryBuildingConnections(
                next = Coordinates(
                    location.x + facing.modX.toDouble(),
                    location.y,
                    location.z + facing.modZ.toDouble()
                ),
                previous = Coordinates(
                    location.x - facing.modX.toDouble(),
                    location.y,
                    location.z - facing.modZ.toDouble()
                )
            )
        }
    }
}
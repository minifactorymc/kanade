package me.tech.kanade.factory.building

import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox

enum class FactoryBuildingStructure(
    val structureId: String,
    val bounds: Bounds
) {
    EPIC_CONVEYOR("epic_conveyor", Bounds.THREE);

    enum class Bounds(
        private val x: Double,
        private val y: Double,
        private val z: Double
    ) {
        ONE(0.0, 1.0, 0.0),
        THREE(1.5, 1.0, 1.5),
        FIVE(2.5, 1.0, 2.5);

        fun toBoundingBox(center: Location): BoundingBox {
            return BoundingBox.of(center, x, y, z)
        }

        // TODO: 10/1/2022 please fix this oh my god hat the hell did i write this is dreadful
        fun getStructureOffset(facing: BlockFace): Coordinates {
            if(this == ONE) {
                return Coordinates(0.0, 0.0, 0.0)
            }

            val coords: Coordinates? = when(facing) {
                BlockFace.NORTH -> {
                    if(this == THREE) {
                        Coordinates(-1.0, 0.0, -1.0)
                    } else {
                        null
                    }
                }
                BlockFace.WEST -> {
                    if(this == THREE) {
                        Coordinates(-1.0, 0.0, 1.0)
                    } else {
                        null
                    }
                }
                BlockFace.SOUTH -> {
                    if(this == THREE) {
                        Coordinates(1.0, 0.0, 1.0)
                    } else {
                        null
                    }
                }
                BlockFace.EAST -> {
                    if(this == THREE) {
                        Coordinates(1.0, 0.0, -1.0)
                    } else {
                        null
                    }
                }
                else -> null
            }

            return coords ?: Coordinates(0.0, 0.0, 0.0)
        }
    }
}
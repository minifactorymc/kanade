package me.tech.kanade.factory.building.structure

import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox

enum class FactoryBuildingStructure(
    val structureFile: String,
    val bounds: NewBounds
) {
    /**
     * @deprecated Please remove this.
     * @internal Please do not use this whatsoever.
     */
    // TODO: 10/2/2022 remove
    @Deprecated("dont touch")
    IMAGINARY("imaginary", NewBounds(0.0, 0.0, 0.0)),

    EPIC_CONVEYOR("epic_conveyor", NewBounds(0.0, 0.0, 0.0)),
    COOL_CONVEYOR("cool_conveyor", NewBounds(1.0, 1.0, 1.0)),

    // TESTS
    HORIZONTAL1X1("tests/horizontal1x1", NewBounds(0.0, 0.0, 0.0)),
    HORIZONTAL3X3("tests/horizontal3x3", NewBounds(1.0, 1.0, 1.0)),

    VERTICAL1X1("tests/vertical1x1", NewBounds(0.0, 0.0, 0.0)),
    VERTICAL3X3("tests/vertical3x3", NewBounds(1.0, 1.0, 1.0)),

    TEST3X3X3PLATFORM("tests/platform3x3x3", NewBounds(1.5, 2.0, 1.5));

    data class NewBounds( // TODO: 10/3/2022 fix offset with new stuff pls
        private val x: Double,
        private val y: Double,
        private val z: Double
    ) {
        val isOne: Boolean
            get() = x == 0.0 && z == 0.0

        fun toBoundingBox(center: Location): BoundingBox {
            return BoundingBox.of(center.clone().toBlockLocation(), x, y + 1.0, z)
        }

        fun getStructureOffset(facing: BlockFace): Coordinates {
            // No offset required.
            if(x == 0.0 && z == 0.0) {
                return Coordinates(0.0, 0.0, 0.0)
            }

            val xOffset = if(x == 1.0) 0.0 else 1.0
            val zOffset = if(z == 1.0) 0.0 else 1.0

            val coords: Coordinates = when(facing) {
                BlockFace.NORTH -> Coordinates(x.unaryMinus() + xOffset, 0.0, z - zOffset)
                BlockFace.WEST -> Coordinates(x - xOffset, 0.0, z - zOffset)
                BlockFace.SOUTH -> Coordinates(x - xOffset, 0.0, z.unaryMinus() + zOffset)
                BlockFace.EAST -> Coordinates(x.unaryMinus() + xOffset, 0.0, z.unaryMinus() + zOffset)
                else -> Coordinates(0.0, 0.0, 0.0)
            }

            return coords
        }
    }

    enum class Bounds(
        private val x: Double,
        private val y: Double,
        private val z: Double
    ) {
        // TODO: 10/2/2022 remove?
        ZERO(0.0, 0.0, 0.0), 
        ONE(0.0, 2.0, 0.0),
        THREE(1.5, 1.0, 1.5),
        FIVE(2.5, 2.0, 2.5);

        fun toBoundingBox(center: Location): BoundingBox {
            return BoundingBox.of(center.clone().toBlockLocation(), x, y, z)
        }

        // TODO: 10/1/2022 please fix this oh my god hat the hell did i write this is dreadful
        fun getStructureOffset(facing: BlockFace): Coordinates {
            if(this == ONE) {
                return Coordinates(0.0, 0.0, 0.0)
            }

            val coords: Coordinates? = when(facing) {
                BlockFace.NORTH -> {
                    if(this == THREE) {
                        Coordinates(-1.0, 0.0, 1.0)
                    } else {
                        null
                    }
                }
                BlockFace.WEST -> {
                    if(this == THREE) {
                        Coordinates(1.0, 0.0, 1.0)
                    } else {
                        null
                    }
                }
                BlockFace.SOUTH -> {
                    if(this == THREE) {
                        Coordinates(1.0, 0.0, -1.0)
                    } else {
                        null
                    }
                }
                BlockFace.EAST -> {
                    if(this == THREE) {
                        Coordinates(-1.0, 0.0, -1.0)
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
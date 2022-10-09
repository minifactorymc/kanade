package me.tech.factory.plot.buildings

import me.tech.factory.plot.buildings.impl.tests.horizontal.TestHorizontal1x1Conveyor
import me.tech.factory.plot.buildings.impl.tests.horizontal.TestHorizontal3x3Conveyor
import me.tech.factory.plot.buildings.impl.tests.platforms.Test3x3x3Platform
import me.tech.factory.plot.buildings.impl.tests.vertical.TestVertical3x3Conveyor
import me.tech.factory.plot.buildings.impl.tests.vertical.TestVertical1x1Conveyor
import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.structure.FactoryBuildingStructure
import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.block.BlockFace
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

private val buildings = mutableMapOf<String, KClass<*>>(

    // Tests
    Pair("horizontal1x1", TestHorizontal1x1Conveyor::class),
    Pair("horizontal3x3", TestHorizontal3x3Conveyor::class),

    Pair("vertical1x1", TestVertical1x1Conveyor::class),
    Pair("vertical3x3", TestVertical3x3Conveyor::class),

    Pair("test3x3x3platform", Test3x3x3Platform::class)
)

fun createBuildingInstance(
    buildingId: String,
    location: Location,
    facing: BlockFace
): FactoryBuilding? {
    val building = buildings[buildingId.lowercase()]
        ?: return null

    val coordinates = Coordinates(
        location.x,
        location.y,
        location.z
    )

    val inst = building.primaryConstructor
        ?.call(buildingId.lowercase(), coordinates, facing) as? FactoryBuilding
        ?: return null

    inst.boundingBox = inst.structureBounds.toBoundingBox(location.toCenterLocation())

    return inst
}

fun getImaginaryConveyorLocations(
    bounds: FactoryBuildingStructure.NewBounds,
    center: Location,
    facing: BlockFace,
    vertical: Boolean
): List<Location> {
    // Bounds One doesn't need imaginary conveyors.
    if(bounds.isOne) {
        return emptyList()
    }

    fun generate(amount: Int): List<Location> {
        val locations = mutableListOf<Location>()

        repeat(amount) {
            val y = if(!vertical) {
                0.0
            } else {
                (it * 1.0) + 1.0
            }

            // X
            if(facing == BlockFace.EAST || facing == BlockFace.WEST) {
                locations.addAll(listOf(
                    center.clone().add(it.toDouble() + 1.0, y, 0.0),
                    center.clone().subtract(it.toDouble() + 1.0, y, 0.0)
                ))
            } else if(facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                locations.addAll(listOf(
                    center.clone().add(0.0, y, it.toDouble() + 1.0),
                    center.clone().subtract(0.0, y, it.toDouble() + 1.0)
                ))
            }
        }

        return locations
    }

    // TODO: 10/3/2022 redo pls.
    return generate(1)
//    return when(bounds) {
//        FactoryBuildingStructure.Bounds.THREE -> generate(1)
//        FactoryBuildingStructure.Bounds.FIVE -> generate(2)
//        else -> emptyList()
//    }
}
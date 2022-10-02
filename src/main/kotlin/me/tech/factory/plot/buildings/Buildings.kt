package me.tech.factory.plot.buildings

import me.tech.factory.plot.buildings.impl.CoolConveyor
import me.tech.factory.plot.buildings.impl.EpicConveyor
import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.FactoryBuildingConnections
import me.tech.kanade.factory.building.FactoryBuildingStructure
import me.tech.kanade.factory.building.types.FactoryBuildingMovable
import me.tech.kanade.utils.toCoordinates
import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.block.BlockFace
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

private val buildings = mutableMapOf<String, KClass<*>>(
    Pair("epic_conveyor", EpicConveyor::class),
    Pair("cool_conveyor", CoolConveyor::class)
//    Pair("basic_conveyor", BasicConveyor::class),

//    Pair("basic_upgrader", BasicUpgrader::class)
)

fun createBuildingInstance(
    id: String,
    location: Location,
    facing: BlockFace
): FactoryBuilding? {
    val building = buildings[id.lowercase()]
        ?: return null

    val coordinates = Coordinates(
        location.x,
        location.y,
        location.z
    )
    val connections = FactoryBuildingConnections.fromLocation(location, facing)

    val inst = building.primaryConstructor
        ?.call(coordinates, connections, facing) as? FactoryBuilding
        ?: return null

    inst.boundingBox = inst.structureBounds.toBoundingBox(location.toCenterLocation())

    return inst
}

fun getImaginaryConveyorLocations(
    bounds: FactoryBuildingStructure.Bounds,
    center: Location,
    facing: BlockFace,
): List<Location> {
    // Bounds One doesn't need imaginary conveyors.
    if(bounds == FactoryBuildingStructure.Bounds.ONE) {
        return emptyList()
    }

    fun generate(amount: Int): List<Location> {
        val locations = mutableListOf<Location>()

        repeat(amount) {
            // X
            if(facing == BlockFace.EAST || facing == BlockFace.WEST) {
                locations.addAll(listOf(
                    center.clone().add(it.toDouble() + 1.0, 0.0, 0.0),
                    center.clone().subtract(it.toDouble() + 1.0, 0.0, 0.0)
                ))
            } else if(facing == BlockFace.NORTH || facing == BlockFace.SOUTH) {
                locations.addAll(listOf(
                    center.clone().add(0.0, 0.0, it.toDouble() + 1.0),
                    center.clone().subtract(0.0, 0.0, it.toDouble() + 1.0)
                ))
            }
        }

        return locations
    }

    return when(bounds) {
        FactoryBuildingStructure.Bounds.THREE -> generate(1)
        FactoryBuildingStructure.Bounds.FIVE -> generate(2)
        else -> emptyList()
    }
}
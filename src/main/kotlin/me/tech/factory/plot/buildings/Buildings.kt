package me.tech.factory.plot.buildings

import me.tech.kanade.factory.building.FactoryBuilding
import me.tech.kanade.factory.building.FactoryBuildingConnections
import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.util.BoundingBox
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

private val buildings = mutableMapOf<String, KClass<*>>(
    Pair("epic_conveyor", EpicConveyor::class)
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

    inst.boundingBox = inst.structureBounds.toBoundingBox(location)
    return inst
}
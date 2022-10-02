package me.tech.kanade.utils

import me.tech.mizuhara.models.Coordinates
import org.bukkit.Location
import org.bukkit.World

fun Coordinates.toLocation(world: World): Location {
    return Location(world, x, y, z)
}

fun Location.toCoordinates(): Coordinates {
    return Coordinates(x, y, z)
}
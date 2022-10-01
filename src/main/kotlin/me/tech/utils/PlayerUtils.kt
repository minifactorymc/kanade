package me.tech.utils

import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

/**
 * Get the cardinal direction of the [Player].
 * @return [BlockFace] that can only be [BlockFace.NORTH], [BlockFace.WEST], [BlockFace.SOUTH] and [BlockFace.EAST].
 */
val Player.cardinalDirection: BlockFace
    get() {
        var rotation = (location.yaw - 90.0) % 360.0
        if(rotation < 0.0) {
            rotation += 360.0
        }

        return if(0.0 <= rotation && rotation < 35.0) {
            BlockFace.WEST
        } else if(45.0 <= rotation && rotation < 135.0) {
            BlockFace.NORTH
        } else if(135.0 <= rotation && rotation < 225.0) {
            BlockFace.EAST
        } else if(225.0 <= rotation && rotation < 315.0) {
            BlockFace.SOUTH
        } else {
            BlockFace.WEST
        }
    }
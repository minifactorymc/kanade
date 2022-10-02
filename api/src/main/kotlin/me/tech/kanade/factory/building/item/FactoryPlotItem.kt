package me.tech.kanade.factory.building.item

import org.bukkit.entity.ArmorStand
import java.util.UUID

interface FactoryPlotItem {
    val uuid: UUID
        get() = armorStand.uniqueId

    val head: FactoryPlotItemHead

    val armorStand: ArmorStand

    val value: Double
}
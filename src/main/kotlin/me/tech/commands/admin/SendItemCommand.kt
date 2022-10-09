package me.tech.commands.admin

import me.tech.Kanade
import me.tech.kanade.factory.building.item.FactoryBuildingItemHolder
import me.tech.kanade.factory.building.item.FactoryPlotItem
import me.tech.kanade.factory.building.item.FactoryPlotItemHead
import me.tech.translator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class SendItemCommand(
    private val plugin: Kanade
) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if(!sender.hasPermission("core.admin") || sender !is Player) {
            translator.send(sender, "core.invalid_permissions")
            return true
        }

        val p = plugin.profileManager.profiles[sender.uniqueId] ?: return true
        val f = plugin.factoryManager.factories[p.activeFactory] ?: return true

        val b = f.plot.buildings.values.first()
        if(b is FactoryBuildingItemHolder) {
            b.carriedItem = object : FactoryPlotItem {
                override val head = FactoryPlotItemHead.STONE

                override val armorStand = (sender.world.spawnEntity(sender.location, EntityType.ARMOR_STAND) as ArmorStand).apply {
                    setGravity(false)
                    setAI(false)
                    setCanTick(false)
                    setCanMove(false)
                }

                override val value = 0.0
            }
        }

        return true
    }
}
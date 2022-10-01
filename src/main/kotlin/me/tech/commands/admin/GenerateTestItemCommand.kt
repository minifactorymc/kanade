package me.tech.commands.admin

import io.github.bananapuncher714.nbteditor.NBTEditor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class GenerateTestItemCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): Boolean {
        if(!sender.hasPermission("core.admin") || sender !is Player) {
            return true
        }

        if(args == null || args.isEmpty()) {
            sender.sendMessage("Usage: /generatetestitem <building_id>.")
            return true
        }

        val item = NBTEditor.set(ItemStack(Material.STONE), args[0], "minifactory", "building", "id")
        sender.inventory.addItem(item)
        sender.sendMessage("added ${args[0]}")

        return true
    }

}
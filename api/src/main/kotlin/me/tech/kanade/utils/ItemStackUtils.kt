package me.tech.kanade.utils

import io.github.bananapuncher714.nbteditor.NBTEditor
import org.bukkit.inventory.ItemStack

val ItemStack.buildingId: String?
    get() = NBTEditor.getString(this, "minifactory", "building", "id")
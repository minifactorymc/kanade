package me.tech.listeners

import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.requests.profile.SaveProfileInfoRequest
import me.tech.profile.ProfileManagerImpl
import me.tech.servicecore.isFailure
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(
    private val profileManager: ProfileManagerImpl
) : Listener {
    @EventHandler
    suspend fun onPlayerQuit(ev: PlayerQuitEvent) {
        val uuid = ev.player.uniqueId

        val profile = profileManager.profiles[uuid]
            ?: throw NullPointerException("player left with null profile.")

        val res = MinifactoryAPI.saveProfileInformation(
            uuid,
            SaveProfileInfoRequest(
                profile.lastFactory
            )
        )

        // TODO: 9/28/2022 "tmp"
        if(res.isFailure()) {
            Bukkit.getLogger().severe("API Error -> ${res.message}")
        }

        profileManager.remove(uuid)
    }
}
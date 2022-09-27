package me.tech.listeners

import me.tech.factory.FactoryManagerImpl
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.mongo.ProfileDocument
import me.tech.mm
import me.tech.profile.ProfileManagerImpl
import me.tech.profile.toProfile
import me.tech.servicecore.ServiceResponse
import me.tech.servicecore.isSuccess
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import java.util.UUID
import kotlin.contracts.contract

class PlayerConnectListener(
    private val profileManager: ProfileManagerImpl
) : Listener {
    private val connections = mutableMapOf<UUID, ProfileDocument>()

    @EventHandler
    suspend fun onPlayerPreLogin(ev: AsyncPlayerPreLoginEvent) {
        val res = MinifactoryAPI.getProfile(ev.uniqueId)

        if(res is ServiceResponse.Failure) {
            ev.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                mm.deserialize("<red>Sorry! We were unable to load your player profile at the pre-login stage, contact an administrator.")
            )
            return
        }

        connections[ev.uniqueId] = res.data!!
    }

    @EventHandler
    fun onPlayerLogin(ev: PlayerLoginEvent) {
        val document = connections[ev.player.uniqueId] ?: run {
            // how'd dat hapn?
            ev.disallow(
                PlayerLoginEvent.Result.KICK_OTHER,
                mm.deserialize("<red>Unable to fetch profile at login stage, please contact an administrator.")
            )
            return
        }

        profileManager.add(document.toProfile())
        // add permission attachment & call a profile loaded event.

        connections.remove(ev.player.uniqueId)
    }
}
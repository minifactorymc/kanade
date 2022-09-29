package me.tech.listeners

import kotlinx.coroutines.runBlocking
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.mongo.ProfileDocument
import me.tech.mm
import me.tech.profile.ProfileManagerImpl
import me.tech.profile.toProfile
import me.tech.servicecore.isFailure
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerLoginEvent
import java.util.UUID

class PlayerConnectListener(
    private val profileManager: ProfileManagerImpl
) : Listener {
    private val connections = HashMap<UUID, ProfileDocument>()

    @EventHandler
    fun onPlayerPreLogin(ev: AsyncPlayerPreLoginEvent) {
        runBlocking {
            val res = MinifactoryAPI.getProfile(ev.uniqueId)

            if(res.isFailure()) {
                ev.disallow(
                    AsyncPlayerPreLoginEvent.Result.KICK_OTHER,
                    mm.deserialize("<red>Unable to fetch profile at pre-login stage, please contact an administrator.")
                )
                return@runBlocking
            }

            connections[ev.uniqueId] = res.data!!
        }
    }

    @EventHandler
    fun onPlayerLogin(ev: PlayerLoginEvent) {
        val uuid = ev.player.uniqueId
        val document = connections[uuid] ?: run {
            ev.disallow(
                PlayerLoginEvent.Result.KICK_OTHER,
                mm.deserialize("<red>Unable to fetch profile at login stage, please contact an administrator.")
            )
            return
        }

        profileManager.add(document.toProfile())
        // permission attachment

        connections.remove(uuid)
    }
}
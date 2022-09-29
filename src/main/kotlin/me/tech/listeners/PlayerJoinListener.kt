package me.tech.listeners

import me.tech.factory.FactoryImpl
import me.tech.factory.FactoryManagerImpl
import me.tech.factory.toFactory
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import me.tech.mizuhara.models.requests.factory.CreateFactoryRequest
import me.tech.mm
import me.tech.profile.ProfileImpl
import me.tech.profile.ProfileManagerImpl
import me.tech.servicecore.ServiceResponse
import me.tech.servicecore.isFailure
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.UUID

class PlayerJoinListener(
    private val profileManager: ProfileManagerImpl,
    private val factoryManager: FactoryManagerImpl
) : Listener {
    private val profiles
        get() = profileManager.profiles

    private val factories
        get() = factoryManager.factories

    @EventHandler
    suspend fun onPlayerJoin(ev: PlayerJoinEvent) {
        val player = ev.player
        val uuid = player.uniqueId

        val profile = profiles[uuid] ?: run {
            ev.player.kick(mm.deserialize("<red>Unable to fetch profile at join stage, please contact an administrator."))
            return
        }

        val wasBrought = bringToFactory(player, profile)
        // Create new factory.
        if(!wasBrought) {
            // TODO: 9/28/2022 We can definitely merge this into one function call, for now this works for testing.
            val res = MinifactoryAPI.createFactory(CreateFactoryRequest(
                uuid
            ))

            if(!res.success) {
                player.kick(mm.deserialize("<red>API Error -> Failed to create new factory."))
                return
            }

            val factory = factoryManager.load(res.data!!)
            profile.activeFactory = factory.id
            player.teleport(factory.plot.center)
        }
    }

    private suspend fun bringToFactory(
        player: Player,
        profile: ProfileImpl
    ): Boolean {
        fun loadFactorySession(
            res: ServiceResponse<FactoryDocument>
        ): FactoryImpl? {
            if(res.isFailure()) {
                return null
            }

            return factoryManager.load(res.data!!)
        }

        val lastFactoryId = profile.lastFactory
        if(
            lastFactoryId == null
            || !profile.factories.contains(lastFactoryId)
        ) {
            return false
        }

        val factory = factories.getOrDefault(lastFactoryId, null)
            ?: loadFactorySession(MinifactoryAPI.getFactory(lastFactoryId))
            // Create a new factory or something.
            ?: return false

        // tmp
        profile.activeFactory = factory.id
        player.teleport(factory.plot.center)

        return true
    }
}
package me.tech.listeners

import me.tech.factory.FactoryImpl
import me.tech.factory.FactoryManagerImpl
import me.tech.factory.toFactory
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import me.tech.mm
import me.tech.profile.ProfileManagerImpl
import me.tech.servicecore.ServiceResponse
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
        val uuid = ev.player.uniqueId

        val profile = profiles[uuid] ?: run {
            ev.player.kick(mm.deserialize("<red>Unable to fetch profile at join stage, please contact an administrator."))
            return
        }


        if(
            profile.lastFactory != null
            && profile.factories.contains(profile.lastFactory)
        ) {
            val factory = handleFactoryLoading(
                uuid,
                MinifactoryAPI.getFactory(profile.lastFactory)
            )

            // load/send to factory.
            return
        }

        // open gui to select profile
    }

    private fun handleFactoryLoading(
        uuid: UUID,
        res: ServiceResponse<FactoryDocument>
    ): FactoryImpl? {
        if(res is ServiceResponse.Failure) {
            return null
        }
        val document = res.data!!

        val possibleLoadedFactory = factories[document.id]
        if(possibleLoadedFactory != null) {
            return possibleLoadedFactory
        }

        return factoryManager.load(document)
    }
}
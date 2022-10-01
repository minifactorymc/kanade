package me.tech.listeners

import me.tech.Kanade
import me.tech.factory.FactoryManagerImpl
import me.tech.mizuhara.MinifactoryAPI
import me.tech.mizuhara.models.requests.profile.SaveProfileInfoRequest
import me.tech.profile.ProfileManagerImpl
import me.tech.servicecore.isFailure
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerQuitListener(
    private val profileManager: ProfileManagerImpl,
    private val factoryManager: FactoryManagerImpl
) : Listener {
    private val profiles
        get() = profileManager.profiles

    private val factories
        get() = factoryManager.factories

    @EventHandler
    suspend fun onPlayerQuit(ev: PlayerQuitEvent) {
        val uuid = ev.player.uniqueId

        val profile = profiles[uuid]
            ?: throw NullPointerException("player left with null profile.")

        // TODO: 9/29/2022 tmp
        val f = factories[profile.lastFactory]!!
        factoryManager.save(f)

        factoryManager.remove(profile.lastFactory!!)

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
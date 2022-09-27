package me.tech.profile

import java.util.*

class ProfileManagerImpl {
    private val _profiles = mutableMapOf<UUID, ProfileImpl>()
    val profiles: Map<UUID, ProfileImpl>
        get() = Collections.unmodifiableMap(_profiles)

    fun add(profile: ProfileImpl) {
        _profiles[profile.uuid] = profile
    }
}
package me.tech.profile

import me.tech.mizuhara.models.mongo.ProfileDocument
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Bukkit
import org.litote.kmongo.Id
import java.util.UUID

class ProfileImpl(
    val uuid: UUID,
    val rank: String,
    var lastFactory: Id<FactoryDocument>?,
    val factories: List<Id<FactoryDocument>>
) {
    var activeFactory: Id<FactoryDocument>? = null
        set(value) {
            lastFactory = value
            field = value
        }
}

fun ProfileDocument.toProfile(): ProfileImpl {
    return ProfileImpl(
        this.uuid,
        this.rank,
        this.lastFactory,
        this.factories
    )
}
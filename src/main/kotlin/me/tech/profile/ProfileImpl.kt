package me.tech.profile

import me.tech.mizuhara.models.mongo.ProfileDocument
import me.tech.mizuhara.models.mongo.factory.FactoryDocument
import org.bukkit.Bukkit
import org.litote.kmongo.Id
import java.util.UUID

class ProfileImpl(
    val uuid: UUID,
    val rank: String,
    val lastFactory: Id<FactoryDocument>?,
    val factories: List<Id<FactoryDocument>>
) {
    fun a() {
        Bukkit.getWorld("A")
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
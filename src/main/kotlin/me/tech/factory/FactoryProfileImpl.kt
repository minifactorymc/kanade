package me.tech.factory

import me.tech.kanade.factory.FactoryProfile
import me.tech.mizuhara.models.mongo.factory.FactoryProfileDocument
import java.util.UUID

class FactoryProfileImpl(
    override val uuid: UUID,
    override val balance: Int
): FactoryProfile {
}

fun FactoryProfileDocument.toFactoryProfile(): FactoryProfileImpl {
    return FactoryProfileImpl(
        this.uuid,
        this.balance
    )
}
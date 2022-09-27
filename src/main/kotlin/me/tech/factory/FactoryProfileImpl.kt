package me.tech.factory

import me.tech.mizuhara.models.mongo.factory.FactoryProfileDocument
import java.util.UUID

class FactoryProfileImpl(
    val uuid: UUID,
    val balance: Int
) {
}

fun FactoryProfileDocument.toFactoryProfile(): FactoryProfileImpl {
    return FactoryProfileImpl(
        this.uuid,
        this.balance
    )
}
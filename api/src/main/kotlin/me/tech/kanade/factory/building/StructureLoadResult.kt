package me.tech.kanade.factory.building

enum class StructureLoadResult {
    OUTSIDE_PLOT,
    INTERSECTING,
    EXCEPTION,
    SUCCESS;

    fun isSuccess(): Boolean {
        return this == SUCCESS
    }
}
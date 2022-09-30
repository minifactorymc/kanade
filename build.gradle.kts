plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.tech"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    group = "me.tech"
    version = "0.0.1"

    repositories {
        mavenCentral()
        mavenLocal()

        maven("https://repo.purpurmc.org/snapshots")
        maven("https://repo.codemc.org/repository/maven-public/")
    }

    dependencies {
        implementation("me.tech", "mizuhara", "0.0.1")
        implementation("org.litote.kmongo", "kmongo-id", "4.7.1")

        implementation("io.github.bananapuncher714", "nbteditor", "7.18.3")

        implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.6.4")

        compileOnly("org.purpurmc.purpur", "purpur-api", "1.19.2-R0.1-SNAPSHOT")
    }
}


dependencies {
    implementation(project(":api"))

    implementation("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-api", "2.5.0")
    implementation("com.github.shynixn.mccoroutine", "mccoroutine-bukkit-core", "2.5.0")

    implementation("me.tech", "chestuiplus", "1.0.0")
    implementation("me.tech", "azusa", "0.0.1")
    implementation("org.mongodb", "bson", "4.7.1")
    compileOnly("me.tech", "service-core", "0.0.1")

    implementation("net.kyori", "adventure-api", "4.11.0")

    compileOnly("com.github.shynixn.structureblocklib", "structureblocklib-bukkit-api", "2.8.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.10")
}

bukkit {
    name = "kanade"
    description = "Provided core for MiniFactory"
    authors = listOf("Tech")
    main = "me.tech.Kanade"
    apiVersion = "1.19"
    libraries = listOf(
        "com.github.shynixn.structureblocklib:structureblocklib-bukkit-api:2.8.0",
        "com.github.shynixn.structureblocklib:structureblocklib-bukkit-core:2.8.0"
    )
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    commands {
        register("world")
        register("loadfactoryfromid")
        register("pregenerateplotsets")
    }
}
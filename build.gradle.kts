import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT"
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("dev.deftu.gradle.bloom") version "0.2.0"
}

val modid = property("mod.id")
val modname = property("mod.name")
val modversion = property("mod.version")
val mcversion = property("minecraft_version")

base {
    archivesName.set(property("mod.id") as String)
}

repositories {
    maven("https://maven.parchmentmc.org")
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://maven.gegy.dev/releases")
}

loom {
    runConfigs.all {
        ideConfigGenerated(stonecutter.current.isActive)
        runDir = "../../run" // This sets the run folder for all mc versions to the same folder. Remove this line if you want individual run folders.
    }

    runConfigs.remove(runConfigs["server"]) // Removes server run configs
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    @Suppress("UnstableApiUsage")
    mappings(loom.layered {
        officialMojangMappings()
        optionalProp("${property("parchment_version")}") {
            parchment("org.parchmentmc.data:parchment-${property("minecraft_version")}:$it@zip")
        }
        optionalProp("${property("yalmm_version")}") {
            mappings("dev.lambdaurora:yalmm-mojbackward:${property("minecraft_version")}+build.$it")
        }
    })
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    modImplementation("org.polyfrost.oneconfig:${property("minecraft_version")}-fabric:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:commands:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:config:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:config-impl:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:events:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:internal:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:ui:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:utils:1.0.0-alpha.181")
    modImplementation("org.polyfrost.oneconfig:hud:1.0.0-alpha.181")
}

bloom {
    replacement("@MOD_ID@", modid!!)
    replacement("@MOD_NAME@", modname!!)
    replacement("@MOD_VERSION@", modversion!!)
}

tasks.processResources {
    val props = mapOf(
        "mod_id" to modid,
        "mod_name" to modname,
        "mod_version" to modversion,
        "mc_version" to mcversion,
        "loader_version" to providers.gradleProperty("loader_version").get()
    )

    inputs.properties(props)

    filesMatching("fabric.mod.json") {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
}

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", base.archivesName)

    from("LICENSE") {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

fun <T> optionalProp(property: String, block: (String) -> T?): T? =
    findProperty(property)?.toString()?.takeUnless { it.isBlank() }?.let(block)
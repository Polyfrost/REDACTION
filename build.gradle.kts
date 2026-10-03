import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import net.ornithemc.ploceus.api.PloceusGradleExtensionApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("dev.kikugie.loom-back-compat")
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT" apply false
    id("ploceus") version "1.17.4" apply false
    id("dev.deftu.gradle.bloom") version "0.2.0"
    id("me.modmuss50.mod-publish-plugin") version "2.2.0"
}

val isOrnithe = sc.current.version == "1.8.9"
val ploceus = if (isOrnithe) {
    pluginManager.apply("net.fabricmc.fabric-loom-remap")
    pluginManager.apply("ploceus")

    configurations.configureEach {
        exclude(group = "org.lwjgl.lwjgl")
    }

    extensions.getByType<PloceusGradleExtensionApi>().apply {
        setIntermediaryGeneration(2)
    }
} else {
    null
}

val modid: String = sc.properties["mod.id"]
val modname: String = sc.properties["mod.name"]
val modversion: String = sc.properties["mod.version"]
val moddescription: String = sc.properties["mod.description"]
val mcversion: String = sc.current.version
val versionrange: String = sc.properties.getOrNull<String>("mod.mc_compat") ?: mcversion
val loaderversion: String = sc.properties["deps.fabric_loader"]
val oneconfigversion: String = sc.properties["deps.oneconfig"]
val loader = if (isOrnithe) "ornithe" else "fabric"

version = "$modversion+$mcversion"
base.archivesName = modname

val requiredJava: JavaVersion = when {
    isOrnithe || sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    else -> JavaVersion.VERSION_21
}

val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map { it.toString() }

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }

    mavenCentral()
    google()
    maven("https://repo.polyfrost.org/releases") { name = "Polyfrost Releases" }
    maven("https://repo.polyfrost.org/snapshots") { name = "Polyfrost Snapshots" }
    maven("https://central.sonatype.com/repository/maven-snapshots") {
        name = "Sonatype Snapshots"
        content { includeGroup("net.kyori") }
    }
    maven("https://maven.cloverclient.com/releases") {
        content { includeGroup("pl.tomgirl") }
    }
    strictMaven("https://maven.deftu.dev/releases", "Deftu", "dev.deftu")
    strictMaven("https://maven.terraformersmc.com/", "TerraformersMC", "com.terraformersmc")
    strictMaven("https://maven.fabricmc.net/", "FabricMC", "net.fabricmc")
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcversion")
    if (isOrnithe) {
        mappings(ploceus!!.layeredMappings {
            mappings("net.ornithemc:feather-gen2:$mcversion+build.${sc.properties.get<String>("deps.feather_build")}:v2") {
                containsUnpick()
            }
            mappings(rootProject.file("mappings/feather-overrides.tiny"))
        })
    } else {
        loomx.applyMojangMappings()
    }

    modImplementation("net.fabricmc:fabric-loader:$loaderversion")
    modImplementation("org.polyfrost.oneconfig:$mcversion-$loader:$oneconfigversion")
    for (module in arrayOf("config", "config-impl", "events", "utils")) {
        implementation("org.polyfrost.oneconfig:$module:$oneconfigversion")
    }

    if (!isOrnithe) {
        val fapiversion: String = sc.properties["deps.fabric_api"]
        modImplementation("net.fabricmc.fabric-api:fabric-api:$fapiversion")
    }

    testImplementation("org.junit.jupiter:junit-jupiter:${sc.properties.get<String>("deps.junit")}")
    testImplementation("net.fabricmc:fabric-loader-junit:$loaderversion")
}

// 1.8.9 has its own mixins under versions/1.8.9/src
if (isOrnithe) sourceSets.main {
    java.exclude("org/polyfrost/redaction/mixin/client/*.java", "org/polyfrost/redaction/mixin/client/accessor/*.java")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/${if (isOrnithe) "ornithe" else "fabric"}.mod.json")

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run")
        jvmArguments.add("-Dmixin.debug.export=true")
    }

    runConfigs.remove(runConfigs["server"])
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

val kotlinJvmTarget = JvmTarget.fromTarget(requiredJava.majorVersion)

tasks.withType<JavaCompile>().configureEach {
    options.release = requiredJava.majorVersion.toInt()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = kotlinJvmTarget
}

bloom {
    replacement("@MOD_ID@", modid)
    replacement("@MOD_NAME@", modname)
    replacement("@MOD_VERSION@", modversion)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    processResources {
        val props = mapOf(
            "mod_id" to modid,
            "mod_name" to modname,
            "mod_version" to modversion,
            "mod_description" to moddescription,
            "mc_compat" to versionrange,
            "oneconfig_version" to oneconfigversion
        )

        inputs.properties(props)

        if (isOrnithe) {
            exclude("fabric.mod.json")
            exclude("mixins.*.json")
            rename("ornithe.mod.json", "fabric.mod.json")
            filesMatching("ornithe.mod.json") { expand(props) }
        } else {
            exclude("ornithe.mod.json")
            exclude("legacy-mixins.json")
            filesMatching("fabric.mod.json") { expand(props) }
        }
    }

    jar {
        inputs.property("archivesName", base.archivesName)

        from(rootProject.file("LICENSE")) {
            rename { "${it}_${inputs.properties["archivesName"]}" }
        }
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Builds mod jars and copies results to `build/libs/{mod version}/`"

        inputs.property("version", modversion)
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/$modversion"))
    }
}

val modrinthId = listOf("publish.modrinth.id", "publish.modrinth")
    .firstNotNullOfOrNull { sc.properties.getOrNull<String>(it) ?: findProperty(it)?.toString() }
    ?.takeIf { it.isNotBlank() }
val modrinthToken = listOf("publish.modrinth.token", "modrinth.token")
    .firstNotNullOfOrNull { findProperty(it) }?.toString()?.takeIf { it.isNotBlank() }

val changelogs = rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."

val validateChangelog = tasks.register("validateChangelog") {
    description = "Validates that the changelog is written for the current version."
    group = "publishing"

    if (!changelogs.contains(modversion)) {
        throw GradleException("Changelog for version $modversion not found.")
    }
}

tasks.publishMods.configure {
    dependsOn(validateChangelog)
}
tasks.matching { it.name == "publishModrinth" }.configureEach {
    dependsOn(validateChangelog)
}

// set modrinth token in your user gradle properties
publishMods {
    file = loomx.modJar.flatMap { it.archiveFile }

    displayName = modversion
    version = "v$modversion"
    changelog = changelogs
    type = STABLE

    modLoaders.add(loader)

    dryRun = modrinthId == null || modrinthToken == null

    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = modrinthToken.orEmpty()

            minecraftVersions.addAll(compatibleVersions.ifEmpty { listOf(mcversion) })

            requires("oneconfig", "fabric-language-kotlin")
            if (!isOrnithe) requires("fabric-api")
        }
    }
}

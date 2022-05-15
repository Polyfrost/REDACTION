package net.wyvest.redaction.features.particles

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import net.wyvest.redaction.Redaction
import java.io.File

object Particles {
    private val GSON = GsonBuilder().setPrettyPrinting().create()
    private val PARSER = JsonParser()
    private val file = File(Redaction.modDir, "particles.json")
    var currentRenderingParticle: Particle? = null

    fun initialize() {
        if (!file.exists() || file.readText().isBlank()) {
            file.createNewFile()
            file.writeText("{  }")
        }

        val readJson = PARSER.parse(file.readText()).asJsonObject
        for (particle in Particle.values) {
            if (!readJson.has(particle.particle.particleName)) {
                readJson.add(particle.particle.particleName, PARSER.parse(GSON.toJson(particle)))
            }
        }
        if (!readJson.has("general")) {
            readJson.add("general", PARSER.parse(GSON.toJson(ParticlesConfig())))
        }
        file.writeText(GSON.toJson(readJson))

        try {
            val json = PARSER.parse(file.readText()).asJsonObject
            for (particle in Particle.values) {
                val particleJson = json[particle.particle.particleName].asJsonObject
                particle.enabled = particleJson["enabled"].asBoolean
                particle.tint = particleJson["tint"].asInt
                particle.multiplier = particleJson["multiplier"].asInt
                particle.scale = particleJson["scale"].asInt
            }
            ParticlesConfig.config = GSON.fromJson(json["general"], ParticlesConfig::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            if (!file.delete()) {
                file.writeText("")
            }
            initialize()
        }
    }

    fun writeConfig() {
        val json = PARSER.parse(file.readText()).asJsonObject
        for (entity in Particle.values) {
            json.add(entity.particle.particleName, PARSER.parse(GSON.toJson(entity)))
        }
        json.add("general", PARSER.parse(GSON.toJson(ParticlesConfig.config)))
        file.writeText(GSON.toJson(json))
    }
}
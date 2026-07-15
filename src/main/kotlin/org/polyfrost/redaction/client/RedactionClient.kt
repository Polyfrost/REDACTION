package org.polyfrost.redaction.client

import org.polyfrost.oneconfig.utils.v1.dsl.addDefaultCommand
import org.polyfrost.redaction.client.features.particles.ParticleManager
import org.polyfrost.redaction.client.features.ServerManager

object RedactionClient {
    fun initialize() {
        RedactionConfig.preload()
        RedactionConfig.addDefaultCommand()

        ServerManager.initialize()
        ParticleManager.initialize()
    }
}

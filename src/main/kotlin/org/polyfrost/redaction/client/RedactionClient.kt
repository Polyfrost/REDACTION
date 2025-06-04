package org.polyfrost.redaction.client

import org.polyfrost.oneconfig.api.commands.v1.CommandManager
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
import org.polyfrost.redaction.RedactionConstants
import org.polyfrost.redaction.client.features.particles.ParticleManager
import org.polyfrost.redaction.client.features.ServerManager

object RedactionClient {

    fun initialize() {
        RedactionConfig.preload()
        ServerManager.initialize()
        ParticleManager.initialize()

        CommandManager.register(CommandManager.literal(RedactionConstants.ID).executes { ctx ->
            RedactionConfig.openUI()

            1
        })
    }

}
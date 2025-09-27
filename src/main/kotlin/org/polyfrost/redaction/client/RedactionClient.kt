package org.polyfrost.redaction.client

import com.mojang.brigadier.Command
import dev.deftu.omnicore.api.client.commands.OmniClientCommands
import dev.deftu.omnicore.api.client.commands.command
import org.polyfrost.oneconfig.utils.v1.dsl.openUI
import org.polyfrost.redaction.RedactionConstants
import org.polyfrost.redaction.client.features.particles.ParticleManager
import org.polyfrost.redaction.client.features.ServerManager

object RedactionClient {
    fun initialize() {
        RedactionConfig.preload()

        ServerManager.initialize()
        ParticleManager.initialize()

        OmniClientCommands.command(RedactionConstants.ID) {
            runs {
                RedactionConfig.openUI()
                Command.SINGLE_SUCCESS
            }
        }.register()
    }
}

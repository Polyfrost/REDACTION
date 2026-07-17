package org.polyfrost.redaction

import net.fabricmc.api.ClientModInitializer
import org.polyfrost.redaction.client.RedactionClient

class RedactionEntrypoint : ClientModInitializer {
    override fun onInitializeClient() {
        RedactionClient.initialize()
    }
}

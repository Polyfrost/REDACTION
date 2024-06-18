package org.polyfrost.redaction.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import org.polyfrost.redaction.config.RedactionConfig

@Command("redaction")
class RedactionCommand {
    @Main
    fun handle() {
        RedactionConfig.openGui()
    }
}
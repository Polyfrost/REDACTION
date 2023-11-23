package net.wyvest.redaction.command

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import net.wyvest.redaction.config.RedactionConfig

class RedactionCommand : Command("redaction") {
    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(RedactionConfig.gui())
    }
}
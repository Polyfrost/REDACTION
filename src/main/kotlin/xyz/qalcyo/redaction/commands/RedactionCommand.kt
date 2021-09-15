package xyz.qalcyo.redaction.commands

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import xyz.qalcyo.redaction.Redaction
import xyz.qalcyo.redaction.config.RedactionConfig

@Suppress("unused")
object RedactionCommand : Command(Redaction.ID, true) {

    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(RedactionConfig.gui())
    }

    @SubCommand("config", description = "Opens the config GUI for " + Redaction.NAME)
    fun config() {
        EssentialAPI.getGuiUtil().openScreen(RedactionConfig.gui())
    }
}
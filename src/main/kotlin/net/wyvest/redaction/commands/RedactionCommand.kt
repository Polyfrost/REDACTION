package net.wyvest.redaction.commands

import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.gui.HitboxPreviewGUI

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

    @SubCommand("hitbox", description = "Opens the hitbox config GUI for " + Redaction.NAME)
    fun hitboxes() {
        EssentialAPI.getGuiUtil().openScreen(HitboxPreviewGUI())
    }
}
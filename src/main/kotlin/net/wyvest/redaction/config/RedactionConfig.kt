package net.wyvest.redaction.config

import gg.essential.api.EssentialAPI
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.Redaction.NAME
import net.wyvest.redaction.Redaction.mc
import net.wyvest.redaction.gui.DownloadConfirmGui
import net.wyvest.redaction.utils.Updater
import java.io.File

object RedactionConfig : Vigilant(File("config/Wyvest/$NAME/${Redaction.ID}.toml"), NAME) {
    @Property(
        type = PropertyType.SWITCH,
        name = "Show Update Notification",
        description = "Show a notification when you start Minecraft informing you of new updates.",
        category = "Updater"
    )
    var showUpdateNotification = true

    @Property(
        type = PropertyType.BUTTON,
        name = "Update Now",
        description = "Update $NAME by clicking the button.",
        category = "Updater"
    )
    fun update() {
        if (Updater.shouldUpdate) EssentialAPI.getGuiUtil()
            .openScreen(DownloadConfirmGui(mc.currentScreen)) else EssentialAPI.getNotifications()
            .push(NAME, "No update had been detected at startup, and thus the update GUI has not been shown.")
    }
}
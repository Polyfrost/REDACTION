package xyz.qalcyo.redaction.config

import gg.essential.api.EssentialAPI
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import xyz.qalcyo.redaction.Redaction
import xyz.qalcyo.redaction.Redaction.NAME
import xyz.qalcyo.redaction.Redaction.mc
import xyz.qalcyo.redaction.gui.DownloadConfirmGui
import xyz.qalcyo.redaction.utils.Updater
import java.awt.Color
import java.io.File

object RedactionConfig : Vigilant(File(Redaction.modDir, "${Redaction.ID}.toml"), NAME) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Force Text Shadow",
        description = "Forcefully enables the text shadow for all text.\nCan negatively affect performance.",
        category = "General"
    )
    var forceShadow = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Stop Forcing When GUI Opened",
        description = "Return text to its original form when a GUI is opened.",
        category = "General"
    )
    var guiOpenShadow = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Blackbar",
        description = "Replace the hotbar with a cleaner blackbar.",
        category = "Blackbar"
    )
    var blackbar = false

    @Property(
        type = PropertyType.COLOR,
        name = "Blackbar Color",
        description = "Choose the color for the blackbar.",
        category = "Blackbar"
    )
    var blackbarColor: Color = Color(0, 0, 0, 85)

    @Property(
        type = PropertyType.COLOR,
        name = "Blackbar Item Highlight Color",
        description = "Choose the color for the blackbar item highlight color",
        category = "Blackbar"
    )
    var blackbarItemColor: Color = Color.WHITE

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Update Notification",
        description = "Show a notification when you start Minecraft informing you of new updates.",
        category = "Updater"
    )
    var showUpdateNotification = true

    @Suppress("unused")
    @Property(
        type = PropertyType.BUTTON,
        name = "Update Now",
        description = "Update $NAME by clicking the button.",
        category = "Updater"
    )
    private fun update() {
        if (Updater.shouldUpdate) EssentialAPI.getGuiUtil()
            .openScreen(DownloadConfirmGui(mc.currentScreen)) else EssentialAPI.getNotifications()
            .push(NAME, "No update had been detected at startup, and thus the update GUI has not been shown.")
    }
}
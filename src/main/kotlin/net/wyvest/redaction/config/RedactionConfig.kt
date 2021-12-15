package net.wyvest.redaction.config

import gg.essential.api.EssentialAPI
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.Redaction.NAME
import net.wyvest.redaction.Redaction.hasChanged
import net.wyvest.redaction.Redaction.mc
import net.wyvest.redaction.gui.DownloadConfirmGui
import net.wyvest.redaction.hud.HudManager
import net.wyvest.redaction.utils.Updater
import java.awt.Color
import java.io.File

object RedactionConfig : Vigilant(File(Redaction.modDir, "${Redaction.ID}.toml"), NAME) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Fix Skin Rendering",
        description = "Fix a skin rendering bug where transparent skins will not be transparent.",
        category = "General"
    )
    var fixSkinRendering = true

    @Property(
        type = PropertyType.SWITCH,
        name = "Server Preview in Direct Connect",
        description = "Show a server preview in the direct connect GUI.",
        category = "General"
    )
    var serverPreview = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Blackbar",
        description = "Replace the hotbar with a cleaner blackbar.",
        category = "Blackbar"
    )
    var blackbar = false

    @Property(
        type = PropertyType.SLIDER,
        name = "Blackbar Update Speed",
        description = "Select the speed of the blackbar animation updating. Measured in milliseconds",
        category = "Blackbar",
        min = 10,
        max = 100
    )
    var blackbarSpeed = 10

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
        name = "Add Snow in Inventory",
        description = "Add snow in the inventory.",
        category = "Inventory"
    )
    var addSnow = false

    @Property(
        type = PropertyType.SLIDER,
        name = "Amount of Particles",
        description = "Modify the amount of snow / particles in the inventory.",
        category = "Inventory",
        min = 50,
        max = 1000
    )
    var particles = 100

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Blackbar Item Highlight Second Color",
        description = "Show blackbar item highlight second color.",
        category = "Blackbar"
    )
    var blackbarSecondColor = true

    @Property(
        type = PropertyType.COLOR,
        name = "Blackbar Item Highlight Second Color",
        description = "Choose the color for the blackbar item highlight second color",
        category = "Blackbar"
    )
    var blackbarItemColor2 : Color = Color.BLACK

    @Property(
        type = PropertyType.SWITCH,
        name = "Hide Blackbar",
        description = "Hide blackbar when chat is open",
        category = "Blackbar"
    )
    var hideBlackbar = true

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

    init {
        initialize()
        registerListener("particles") { newValue: Int ->
            particles = newValue
            hasChanged = true
        }
        registerListener("blackbarSpeed") { newValue: Int ->
            blackbarSpeed = newValue
            HudManager.elements[0].setTimer()
        }
    }
}
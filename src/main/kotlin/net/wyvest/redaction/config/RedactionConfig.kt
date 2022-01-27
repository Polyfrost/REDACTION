package net.wyvest.redaction.config

import gg.essential.api.EssentialAPI
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import net.minecraft.client.Minecraft
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.Redaction.NAME
import net.wyvest.redaction.Redaction.mc
import net.wyvest.redaction.features.BlackBar
import net.wyvest.redaction.features.NameHighlight
import net.wyvest.redaction.features.ParticleManager
import net.wyvest.redaction.gui.DownloadConfirmGui
import net.wyvest.redaction.gui.HitboxPreviewGUI
import net.wyvest.redaction.utils.Updater
import java.awt.Color
import java.io.File

object RedactionConfig : Vigilant(File(Redaction.modDir, "${Redaction.ID}.toml"), NAME) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Server Preview in Direct Connect",
        description = "Show a server preview in the direct connect GUI.",
        category = "General"
    )
    var serverPreview = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Last Server Joined Button",
        description = "Show a last server joined button in the main menu.",
        category = "General"
    )
    var lastServerJoined = false

    @Property(
        type = PropertyType.TEXT,
        name = "Last Server Joined IP",
        description = "yeah",
        category = "General",
        hidden = true
    )
    var lastServerIP = ""

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
        type = PropertyType.BUTTON,
        name = "Hitbox GUI",
        description = "Show the Hitbox Config GUI.",
        category = "Hitboxes"
    )
    private fun showHitboxGUI() {
        // gui autoscaling thing screws positioning up somehow lol so do this weird hack
        Minecraft.getMinecraft().displayGuiScreen(null)
        Minecraft.getMinecraft().addScheduledTask {
            Minecraft.getMinecraft().addScheduledTask {
                EssentialAPI.getGuiUtil().openScreen(HitboxPreviewGUI(true))
            }
        }
    }

    @Property(
        type = PropertyType.SWITCH,
        name = "Highlight Name",
        description = "Highlight your name.",
        category = "Highlight"
    )
    var highlightName = false

    @Property(
        type = PropertyType.SELECTOR,
        name = "Text Color",
        description = "Change the text color for the highlight.",
        category = "Highlight",
        options = ["Black", "Dark Blue", "Dark Green", "Dark Aqua", "Dark Red", "Dark Purple", "Gold", "Gray", "Dark Gray", "Blue", "Green", "Aqua", "Red", "Light Purple", "Yellow", "White"]
    )
    var textColor = 0

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
            ParticleManager.hasChanged = true
        }
        registerListener("blackbarSpeed") { newValue: Int ->
            blackbarSpeed = newValue
            BlackBar.setTimer()
        }
        registerListener("textColor") { color: Int ->
            textColor = color // update immediately
            NameHighlight.colorDelegate.invalidate()
        }
    }
}
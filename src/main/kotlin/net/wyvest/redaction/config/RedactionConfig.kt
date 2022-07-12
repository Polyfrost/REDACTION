package net.wyvest.redaction.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.Checkbox
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator
import cc.polyfrost.oneconfig.utils.Multithreading
import cc.polyfrost.oneconfig.utils.gui.GuiUtils
import net.minecraft.client.Minecraft
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.features.BlackBar
import net.wyvest.redaction.features.NameHighlight
import net.wyvest.redaction.features.ParticleManager
import net.wyvest.redaction.gui.HitboxPreviewGUI
import java.awt.Color
import java.io.File

object RedactionConfig : Config(
    Mod(
        Redaction.NAME,
        ModType.UTIL_QOL,
        VigilanceMigrator(File(Redaction.modDir, "${Redaction.ID}.toml").path)
    ), "redaction.json"
) {

    @Switch(
        name = "Disable Hand Item Lighting",
        category = "General"
    )
    var disableHandLighting = false

    @Switch(
        name = "Customize Hand Item FOV",
        category = "General"
    )
    var customHandFOV = false

    @Slider(
        name = "Hand Item FOV",
        category = "General",
        min = 0F,
        max = 180F
    )
    var handFOV = 125

    @Switch(
        name = "Server Preview in Direct Connect",
        category = "General"
    )
    var serverPreview = false

    @Checkbox(
        name = "Last Server Joined Button",
        category = "General"
    )
    var lastServerJoined = false


    var lastServerIP = ""

    @Switch(
        name = "Replace Hotbar with Blackbar",
        category = "Blackbar"
    )
    var blackbar = false

    @Checkbox(
        name = "Blackbar Slot Numbers",
        category = "Blackbar"
    )
    var blackbarSlotNumbers = false

    @Slider(
        name = "Blackbar Update Speed",
        category = "Blackbar",
        min = 1F,
        max = 100F
    )
    var blackbarSpeed = 10

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Blackbar Color",
        category = "Blackbar"
    )
    var blackbarColor: OneColor = OneColor(0, 0, 0, 85)

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Blackbar Item Highlight Color",
        category = "Blackbar"
    )
    var blackbarItemColor: OneColor = OneColor(Color.WHITE)

    @Switch(
        name = "Add Snow in Inventory",
        category = "Inventory"
    )
    var addSnow = false

    @Slider(
        name = "Snow Amount",
        category = "Inventory",
        min = 50F,
        max = 1000F
    )
    var particles = 100

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Snow Color",
        category = "Inventory"
    )
    var snowColor = OneColor(-1)

    @Button(
        name = "Open Hitbox GUI",
        category = "Hitboxes", text = "Open"
    )
    var showHitboxGUI = Runnable { // gui autoscaling thing screws positioning up somehow lol so do this weird hack
        Minecraft.getMinecraft().displayGuiScreen(null)
        Multithreading.runAsync {
            Minecraft.getMinecraft().addScheduledTask {
                Multithreading.runAsync {
                    Minecraft.getMinecraft().addScheduledTask {
                        GuiUtils.displayScreen(HitboxPreviewGUI(true))
                    }
                }
            }
        }
    }

    @Checkbox(
        name = "Highlight Name",
        category = "Highlight"
    )
    var highlightName = false

    @Checkbox(
        name = "Bold Name",
        category = "Highlight"
    )
    var boldName = false

    @Checkbox(
        name = "Italics Name",
        category = "Highlight"
    )
    var italicsName = false

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Text Color",
        category = "Highlight",
        allowAlpha = false
    )
    var textColor: OneColor = OneColor(Color.BLACK)

    @Switch(
        name = "Highlight Async (improves performance)",
        category = "Highlight"
    )
    var asyncHighlight = true

    fun getRedText(shadow: Boolean): Int {
        var red = textColor.red
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            red = (red * 30 + textColor.green * 59 + textColor.blue * 11) / 100
        }

        if (shadow) {
            red /= 4
        }

        return red
    }

    fun getGreenText(shadow: Boolean): Int {
        var green = textColor.green
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            green = (textColor.red * 30 + textColor.green * 70) / 100
        }

        if (shadow) {
            green /= 4
        }

        return green
    }

    fun getBlueText(shadow: Boolean): Int {
        var blue = textColor.blue
        if (Minecraft.getMinecraft().gameSettings.anaglyph) {
            blue = (textColor.red * 30 + blue * 70) / 100
        }

        if (shadow) {
            blue /= 4
        }

        return blue
    }

    init {
        initialize()
        addListener("particles") {
            ParticleManager.hasChanged = true
        }
        addListener("blackbarSpeed") {
            BlackBar.setTimer()
        }
        addListener("boldName") {
            NameHighlight.cache.invalidateAll()
        }
        addListener("italicsName") {
            NameHighlight.cache.invalidateAll()
        }

        addDependency("handFOV", "customHandFOV")
    }
}
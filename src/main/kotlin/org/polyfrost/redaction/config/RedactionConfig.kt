package org.polyfrost.redaction.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Checkbox
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Slider
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.migration.VigilanceMigrator
import org.polyfrost.redaction.Redaction
import org.polyfrost.redaction.features.BlackBar
import org.polyfrost.redaction.features.ParticleManager
import java.awt.Color
import java.io.File

object RedactionConfig : Config(
    Mod(
        Redaction.NAME,
        ModType.UTIL_QOL,
        VigilanceMigrator(File(File(File("./W-OVERFLOW"), "REDACTION"), "redaction.toml").path)
    ), "${Redaction.ID}.json"
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
        category = "Inventory",
        subcategory = "Snow"
    )
    var addSnow = false

    @Info(
        text = "Higher snow amounts may result in reduced performance!",
        size = 2,
        category = "Inventory",
        subcategory = "Snow",
        type = InfoType.WARNING
    )
    var ignored = false

    @Slider(
        name = "Snow Amount",
        category = "Inventory",
        subcategory = "Snow",
        min = 50F,
        max = 1000F
    )
    var particles = 100

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Snow Color",
        category = "Inventory",
        subcategory = "Snow"
    )
    var snowColor = OneColor(-1)

    @Switch(
        name = "Draw Lines between Snowflakes",
        category = "Inventory",
        subcategory = "Lines"
    )
    var connectSnow = false

    @Slider(
        name = "Line Width",
        category = "Inventory",
        subcategory = "Lines",
        min = 1f,
        max = 5f,
        step = 1
    )
    var lineWidth = 1f

    @cc.polyfrost.oneconfig.config.annotations.Color(
        name = "Line Color",
        category = "Inventory",
        subcategory = "Lines"
    )
    var lineColor = OneColor(Color.WHITE)

    init {
        initialize()
        addListener("particles") {
            ParticleManager.hasChanged = true
        }
        addListener("blackbarSpeed") {
            BlackBar.setTimer()
        }

        addDependency("handFOV", "customHandFOV")

        addDependency("particles", "addSnow")
        addDependency("snowColor", "addSnow")

        addDependency("connectSnow", "addSnow")
        addDependency("lineWidth", "addSnow")
        addDependency("lineWidth", "connectSnow")
        addDependency("lineColor", "addSnow")
        addDependency("lineColor", "connectSnow")

    }

}
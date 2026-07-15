package org.polyfrost.redaction.client

import org.polyfrost.compose.render.PolyColor
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.annotations.Checkbox
import org.polyfrost.oneconfig.api.config.v1.annotations.Color
import org.polyfrost.oneconfig.api.config.v1.annotations.Include
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.redaction.RedactionConstants
import org.polyfrost.redaction.client.features.particles.ParticleManager

object RedactionConfig : Config(
    "${RedactionConstants.ID}.json",
    RedactionConstants.NAME,
    Category.QOL,
) {
    // TODO
//    @Switch(
//        title = "Disable Hand Item Lighting",
//        category = "General"
//    )
//    var disableHandLighting = false

    @Switch(
        title = "Customize Hand Item FOV",
        category = "General"
    )
    var customHandFOV = false

    @Slider(
        title = "Hand Item FOV",
        category = "General",
        min = 0F,
        max = 180F
    )
    var handFOV = 125

    @Switch(
        title = "Server Preview in Direct Connect",
        category = "General"
    )
    var serverPreview = false

    @Checkbox(
        title = "Last Server Joined Button",
        category = "General"
    )
    var lastServerJoined = false

    @Include var lastServerIP = ""

    @Switch(
        title = "Replace Hotbar with Blackbar",
        category = "Blackbar"
    )
    var blackbar = false

    @Checkbox(
        title = "Blackbar Slot Numbers",
        category = "Blackbar"
    )
    var blackbarSlotNumbers = false

    @Color(
        title = "Blackbar Color",
        category = "Blackbar"
    )
    var blackbarColor = PolyColor.BLACK.withAlpha(85)

    @Color(
        title = "Blackbar Item Highlight Color",
        category = "Blackbar"
    )
    var blackbarItemColor = PolyColor.WHITE

    @Switch(
        title = "Add Snow in Inventory",
        category = "Inventory",
        subcategory = "Snow"
    )
    var addSnow = false

    @Slider(
        title = "Snow Amount",
        category = "Inventory",
        subcategory = "Snow",
        min = 50F,
        max = 1000F
    )
    var particles = 100

    @Color(
        title = "Snow Color",
        category = "Inventory",
        subcategory = "Snow",
        alpha = false
    )
    var snowColor = PolyColor.WHITE

    @Switch(
        title = "Draw Lines between Snowflakes",
        category = "Inventory",
        subcategory = "Lines"
    )
    var connectSnow = false

    @Slider(
        title = "Line Width",
        category = "Inventory",
        subcategory = "Lines",
        min = 1f,
        max = 5f,
        step = 1f
    )
    var lineWidth = 1f

    @Color(
        title = "Line Color",
        category = "Inventory",
        subcategory = "Lines",
        alpha = false
    )
    var lineColor = PolyColor.WHITE

    init {
        addCallback("particles") {
            ParticleManager.updateParticles()
        }

        addDependency("handFOV", "customHandFOV")

        listOf(
            "blackbarSlotNumbers", "blackbarColor", "blackbarItemColor"
        ).forEach { addDependency(it, "blackbar") }

        listOf(
            "particles", "snowColor", "connectSnow", "lineWidth", "lineColor"
        ).forEach { addDependency(it, "addSnow") }

        listOf(
            "lineWidth", "lineColor"
        ).forEach { addDependency(it, "connectSnow") }
    }
}

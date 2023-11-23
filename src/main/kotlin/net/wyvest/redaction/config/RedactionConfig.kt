package net.wyvest.redaction.config

import gg.essential.api.EssentialAPI
import gg.essential.api.utils.Multithreading
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import net.minecraft.client.Minecraft
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.Redaction.NAME
import net.wyvest.redaction.features.BlackBar
import net.wyvest.redaction.features.NameHighlight
import net.wyvest.redaction.features.ParticleManager
import net.wyvest.redaction.gui.HitboxPreviewGUI
import java.awt.Color
import java.io.File

object RedactionConfig : Vigilant(File(Redaction.modDir, "${Redaction.ID}.toml"), NAME, sortingBehavior = object :
    SortingBehavior() {
    override fun getCategoryComparator(): Comparator<in Category> = Comparator { o1, o2 ->
        if (o1.name == "General") return@Comparator -1
        if (o2.name == "General") return@Comparator 1
        else compareValuesBy(o1, o2) {
            it.name
        }
    }
}) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Disable Hand Item Lighting",
        description = "Turn off lighting for the hand item.",
        category = "General"
    )
    var disableHandLighting = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Customize Hand Item FOV",
        description = "Change the FOV of which the item held will be rendered.",
        category = "General"
    )
    var customHandFOV = false

    @Property(
        type = PropertyType.NUMBER,
        name = "Hand Item FOV",
        description = "Change the FOV of which the item held will be rendered.",
        category = "General",
        min = 0,
        max = 180
    )
    var handFOV = 125

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
        type = PropertyType.SWITCH,
        name = "Blackbar Slot Numbers",
        // Cephetir: Teach me how to make good descriptions
        description = "Display slot number.",
        category = "Blackbar"
    )
    var blackbarSlotNumbers = false

    @Property(
        type = PropertyType.SLIDER,
        name = "Blackbar Update Speed",
        description = "Select the speed of the blackbar animation updating. Measured in milliseconds.",
        category = "Blackbar",
        min = 1,
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
        name = "Amount of Snow",
        description = "Modify the amount of snow / particles in the inventory.",
        category = "Inventory",
        min = 50,
        max = 1000
    )
    var particles = 100

    @Property(
        type = PropertyType.COLOR,
        name = "Snow Color",
        description = "Modify the color of the snow / particles.",
        category = "Inventory"
    )
    var snowColor = Color(-0x1)

    @Property(
        type = PropertyType.BUTTON,
        name = "Hitbox GUI",
        description = "Show the Hitbox Config GUI.",
        category = "Hitboxes"
    )
    private fun showHitboxGUI() {
        // gui autoscaling thing screws positioning up somehow lol so do this weird hack
        Minecraft.getMinecraft().displayGuiScreen(null)
        Multithreading.runAsync {
            Minecraft.getMinecraft().addScheduledTask {
                Multithreading.runAsync {
                    Minecraft.getMinecraft().addScheduledTask {
                        EssentialAPI.getGuiUtil().openScreen(HitboxPreviewGUI(true))
                    }
                }
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
        type = PropertyType.SWITCH,
        name = "Bold Name",
        description = "Make your name bold when highlighted.",
        category = "Highlight"
    )
    var boldName = false

    @Property(
        type = PropertyType.SWITCH,
        name = "Italics Name",
        description = "Make your name have italics when highlighted.",
        category = "Highlight"
    )
    var italicsName = false

    @Property(
        type = PropertyType.COLOR,
        name = "Text Color",
        description = "Change the text color for the highlight.",
        category = "Highlight",
        allowAlpha = false
    )
    var textColor: Color = Color.BLACK

    @Property(
        type = PropertyType.SWITCH,
        name = "Highlight Async",
        description = "Run highlight code async, which results in significantly better performance.",
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
        registerListener("particles") { newValue: Int ->
            particles = newValue
            ParticleManager.hasChanged = true
        }
        registerListener("blackbarSpeed") { newValue: Int ->
            blackbarSpeed = newValue
            BlackBar.setTimer()
        }
        registerListener("textColor") { color: Color ->
            textColor = color
        }
        registerListener("boldName") { newValue: Boolean ->
            boldName = newValue
            NameHighlight.cache.invalidateAll()
        }
        registerListener("italicsName") { newValue: Boolean ->
            italicsName = newValue
            NameHighlight.cache.invalidateAll()
        }

        addDependency("handFOV", "customHandFOV")
    }
}
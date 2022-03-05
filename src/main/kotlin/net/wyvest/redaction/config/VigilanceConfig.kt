package net.wyvest.redaction.config

import cc.woverflow.onecore.utils.browseURL
import gg.essential.universal.UDesktop
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import net.wyvest.redaction.Redaction
import java.awt.Color
import java.io.File

object VigilanceConfig : Vigilant(File(Redaction.modDir, "vigilance.toml"), "Vigilance Config (from REDACTION)") {
    @Property(PropertyType.BUTTON, "Warning", category = "Color Scheme", subcategory = "Info", description = "This feature will NOT work without an internet connection!\nALL ISSUES RELATING TO THIS MUST GO TO https://woverflow.cc/discord\nClick on the button to learn more.")
    fun internetConnectionWarning() = UDesktop.browseURL("https://github.com/W-OVERFLOW/REDACTION/blob/main/vigilance.md")

    @Property(PropertyType.COLOR, "Bright Divider", category = "Color Scheme", subcategory = "Palette")
    var brightDivider = Color(151, 151, 151)

    @Property(PropertyType.COLOR, "Divider", category = "Color Scheme", subcategory = "Palette")
    var divider = Color(80, 80, 80)

    @Property(PropertyType.COLOR, "Dark Divider", category = "Color Scheme", subcategory = "Palette")
    var darkDivider = Color(50, 50, 50)

    @Property(PropertyType.COLOR, "Outline", category = "Color Scheme", subcategory = "Palette")
    var outline = Color(48, 48, 49)

    @Property(PropertyType.COLOR, "Scroll Bar", category = "Color Scheme", subcategory = "Palette")
    var scrollBar = Color(45, 45, 45)

    @Property(PropertyType.COLOR, "Bright Highlight", category = "Color Scheme", subcategory = "Palette")
    var brightHighlight = Color(50, 50, 50)

    @Property(PropertyType.COLOR, "Highlight", category = "Color Scheme", subcategory = "Palette")
    var highlight = Color(33, 34, 38)

    @Property(PropertyType.COLOR, "Dark Highlight", category = "Color Scheme", subcategory = "Palette")
    var darkHighlight = Color(27, 28, 33)

    @Property(PropertyType.COLOR, "Light Background", category = "Color Scheme", subcategory = "Palette")
    var lightBackground = Color(32, 32, 33)

    @Property(PropertyType.COLOR, "Background", category = "Color Scheme", subcategory = "Palette")
    var background = Color(22, 22, 24)

    @Property(PropertyType.COLOR, "Dark Background", category = "Color Scheme", subcategory = "Palette")
    var darkBackground = Color(10, 10, 11)

    @Property(PropertyType.COLOR, "Search Bar Background", category = "Color Scheme", subcategory = "Palette")
    var searchBarBackground = Color(27, 28, 33)

    @Property(PropertyType.COLOR, "Bright Text", category = "Color Scheme", subcategory = "Palette")
    var brightText = Color(255, 255, 255)

    @Property(PropertyType.COLOR, "Mid Text", category = "Color Scheme", subcategory = "Palette")
    var midText = Color(187, 187, 187)

    @Property(PropertyType.COLOR, "Dark Text", category = "Color Scheme", subcategory = "Palette")
    var darkText = Color(119, 119, 121)

    @Property(PropertyType.COLOR, "Modal Background", category = "Color Scheme", subcategory = "Palette")
    var modalBackground = Color(0, 0, 0, 100)

    @Property(PropertyType.COLOR, "Warning", category = "Color Scheme", subcategory = "Palette")
    var warning = Color(239, 83, 80)

    @Property(PropertyType.COLOR, "Accent", category = "Color Scheme", subcategory = "Palette")
    var accent = Color(1, 165, 82)

    @Property(PropertyType.COLOR, "Success", category = "Color Scheme", subcategory = "Palette")
    var success = Color(1, 165, 82)

    @Property(PropertyType.COLOR, "Transparent", category = "Color Scheme", subcategory = "Palette")
    var transparent = Color(0, 0, 0, 0)

    @Property(PropertyType.COLOR, "Disabled", category = "Color Scheme", subcategory = "Palette")
    var disabled = Color(80, 80, 80)

    init {
        initialize()
    }
}
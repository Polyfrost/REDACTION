package net.wyvest.redaction.gui

import com.mojang.authlib.GameProfile
import gg.essential.api.EssentialAPI
import gg.essential.api.gui.EssentialGUI
import gg.essential.api.gui.buildConfirmationModal
import gg.essential.api.gui.buildEmulatedPlayer
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.ScissorEffect
import gg.essential.universal.wrappers.UPlayer
import gg.essential.vigilance.gui.VigilancePalette
import gg.essential.vigilance.gui.settings.ColorComponent
import gg.essential.vigilance.gui.settings.DropDown
import gg.essential.vigilance.gui.settings.NumberComponent
import gg.essential.vigilance.gui.settings.SwitchComponent
import net.minecraft.client.Minecraft
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.features.hitbox.Entity
import net.wyvest.redaction.features.hitbox.GeneralConfig
import net.wyvest.redaction.features.hitbox.Hitboxes
import java.awt.Color

class HitboxPreviewGUI @JvmOverloads constructor(private val returnToConfigGUI: Boolean = false) : EssentialGUI(version = ElementaVersion.V1, guiTitle = "REDACTION", restorePreviousGuiOnClose = false) {

    private val block by UIBlock(VigilancePalette.getLightBackground()) constrain {
        x = CenterConstraint()
        y = 1.percent()
        width = 100.percent()
        height = 47.percent()
    } childOf content

    private val player by EssentialAPI.getEssentialComponentFactory().buildEmulatedPlayer {
        profile = GameProfile(UPlayer.getUUID(), UPlayer.getPlayer()!!.displayName.formattedText)
    } constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        width = 100.percent()
        height = 100.percent()
    } childOf block effect ScissorEffect(block)

    private val settingsContainer by UIContainer() constrain {
        x = CenterConstraint()
        y = 50.percent()
        width = 100.percent()
        height = 50.percent()
    } childOf content

    private val switchContainer by UIContainer() constrain {
        x = 2.pixels()
        y = 2.pixels()
        width = 100.percent()
        height = 11.pixels()
    } childOf settingsContainer

    private val colorContainer by UIContainer() constrain {
        x = 2.pixels()
        y = SiblingConstraint(10f)
        width = 100.percent()
        height = FillConstraint()
    } childOf settingsContainer

    private val entityDropdown: DropDown by DropDown(1, ArrayList(Entity.map.values.map { it.name }).also { it.add(0, "All") }) constrain {
        x = SiblingConstraint(10f)
        y = 15.percent()
    } childOf titleBar

    private val hitboxWidthText by UIText("Hitbox Width") constrain {
        x = SiblingConstraint(10f)
        y = CenterConstraint()
    } childOf titleBar

    private val hitboxWidthNumber: NumberComponent by NumberComponent(
        GeneralConfig.config.hitboxWidth,
        1,
        20,
        1
    ) constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
    } childOf titleBar

    private val forceHitboxText by UIText("Force Hitbox") constrain {
        x = SiblingConstraint(10f)
        y = CenterConstraint()
    } childOf titleBar

    private val forceHitboxSwitch: SwitchComponent by SwitchComponent(GeneralConfig.config.forceHitbox) constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
    } childOf titleBar

    private val disableForSelfText by UIText("Disable for Self") constrain {
        x = SiblingConstraint(10f)
        y = CenterConstraint()
    } childOf titleBar

    private val disableForSelfSwitch: SwitchComponent by SwitchComponent(GeneralConfig.config.disableForSelf) constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
    } childOf titleBar

    private val accurateHitboxText by UIText("Accurate Hitboxes") constrain {
        x = SiblingConstraint(10f)
        y = CenterConstraint()
    } childOf titleBar

    private val accurateHitboxSwitch: SwitchComponent by SwitchComponent(GeneralConfig.config.accurateHitbox) constrain {
        x = SiblingConstraint(5f)
        y = CenterConstraint()
    } childOf titleBar

    init {
        hitboxWidthNumber.onValueChange {
            GeneralConfig.config.hitboxWidth = it as Int
            Hitboxes.writeConfig()
        }
        forceHitboxSwitch.onValueChange {
            GeneralConfig.config.forceHitbox = it as Boolean
            Hitboxes.writeConfig()
        }
        accurateHitboxSwitch.onValueChange {
            GeneralConfig.config.accurateHitbox = it as Boolean
            Hitboxes.writeConfig()
        }
        disableForSelfSwitch.onValueChange {
            GeneralConfig.config.disableForSelf = it as Boolean
            Hitboxes.writeConfig()
        }
        entityDropdown.onValueChange {
            resetSettings(it)
        }
        resetSettings(1)
    }

    private fun resetSettings(i: Int) {
        if (i == 0) {
            EssentialAPI.getEssentialComponentFactory().buildConfirmationModal {
                text = "Are you sure you want to modify all entity hitboxes?"
                secondaryText = "Changes cannot be reversed!"
                onConfirm = {
                    entityToEmulate = Entity("All", condition = {true}, priority = 0)
                    resetSwitches(i)
                    resetColor(i)
                }
                onDeny = {
                    entityDropdown.select(1)
                }
            } childOf window
        } else {
            entityToEmulate = Entity.map[i] ?: entityToEmulate
            resetSwitches(i)
            resetColor(i)
        }
    }

    private fun resetSwitches(i: Int) {
        switchContainer.children.clear()
        val enabledSwitch by SwitchComponent(Entity.map[if (i == 0) 1 else i]!!.hitboxEnabled) constrain {
            x = 0.pixels()
            y = CenterConstraint()
        } childOf (switchContainer)
        val enabledText by UIText("Hitbox Enabled") constrain {
            x = SiblingConstraint(5f)
            y = CenterConstraint()
        } childOf (switchContainer)
        enabledSwitch.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.hitboxEnabled = value as Boolean
                }
            } else {
                Entity.map[i]!!.hitboxEnabled = !Entity.map[i]!!.hitboxEnabled
            }
            Hitboxes.writeConfig()
        }

        val eyeSwitch by SwitchComponent(Entity.map[if (i == 0) 1 else i]!!.eyeLineEnabled) constrain {
            x = SiblingConstraint(10f)
            y = CenterConstraint()
        } childOf (switchContainer)
        val eyeText by UIText("Eyeline Enabled") constrain {
            x = SiblingConstraint(5f)
            y = CenterConstraint()
        } childOf (switchContainer)
        eyeSwitch.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.eyeLineEnabled = value as Boolean
                }
            } else {
                Entity.map[i]!!.eyeLineEnabled = !Entity.map[i]!!.eyeLineEnabled
            }
            Hitboxes.writeConfig()
        }

        val lineOfSightSwitch by SwitchComponent(Entity.map[if (i == 0) 1 else i]!!.lineEnabled) constrain {
            x = SiblingConstraint(10f)
            y = CenterConstraint()
        } childOf (switchContainer)
        val lineOfSightText by UIText("Line of Sight Enabled") constrain {
            x = SiblingConstraint(5f)
            y = CenterConstraint()
        } childOf (switchContainer)
        lineOfSightSwitch.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.lineEnabled = value as Boolean
                }
            } else {
                Entity.map[i]!!.lineEnabled = !Entity.map[i]!!.lineEnabled
            }
            Hitboxes.writeConfig()
        }
    }

    // this exists because for some reason it shifted sometimes????
    private var eyeLineColorTextX: XConstraint? = null
    private var lineOfSightColorTextX: XConstraint? = null
    private var crosshairColorTextX: XConstraint? = null
    private var eyeLineColorX: XConstraint? = null
    private var lineOfSightColorX: XConstraint? = null
    private var crosshairColorX: XConstraint? = null

    private fun resetColor(i: Int) {
        colorContainer.children.clear()
        val colorText by UIText("Hitbox Color") constrain {
            x = 0.pixels()
            y = 0.pixels()
        } childOf (colorContainer)
        val colorPicker by ColorComponent(Color(Entity.map[if (i == 0) 1 else i]!!.color), false) constrain {
            x = 0.pixels()
            y = (CopyConstraintFloat(true) boundTo colorText) + 10.pixels()
        } childOf (colorContainer)
        colorPicker.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.color = (value as Color).rgb
                }
            } else {
                Entity.map[i]!!.color = (value as Color).rgb
            }
            Hitboxes.writeConfig()
        }

        val crosshairColorText by UIText("Crosshair Color") constrain {
            x = crosshairColorTextX ?: run { crosshairColorTextX = (colorText.getRight() + 5).pixels(); crosshairColorTextX!! }
            y = 0.pixels()
        } childOf (colorContainer)
        val crosshairColorPicker by ColorComponent(Color(Entity.map[if (i == 0) 1 else i]!!.crosshairColor), false) constrain {
            x = crosshairColorX ?: run { crosshairColorX = (crosshairColorText.getLeft() - 34).pixels(); crosshairColorX!! }
            y = (CopyConstraintFloat(true) boundTo crosshairColorText) + 10.pixels()
        } childOf (colorContainer)
        crosshairColorPicker.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.crosshairColor = (value as Color).rgb
                }
            } else {
                Entity.map[i]!!.crosshairColor = (value as Color).rgb
            }
            Hitboxes.writeConfig()
        }

        val eyelineColorText by UIText("Eyeline Color") constrain {
            x = eyeLineColorTextX ?: run { eyeLineColorTextX = (crosshairColorText.getRight() + 5).pixels(); eyeLineColorTextX!! }
            y = 0.pixels()
        } childOf (colorContainer)
        val eyelineColorPicker by ColorComponent(Color(Entity.map[if (i == 0) 1 else i]!!.eyeColor), false) constrain {
            x = eyeLineColorX ?: run { eyeLineColorX = (eyelineColorText.getLeft() - 34).pixels(); eyeLineColorX!! }
            y = (CopyConstraintFloat(true) boundTo eyelineColorText) + 10.pixels()
        } childOf (colorContainer)
        eyelineColorPicker.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.eyeColor = (value as Color).rgb
                }
            } else {
                Entity.map[i]!!.eyeColor = (value as Color).rgb
            }
            Hitboxes.writeConfig()
        }

        val lineOfSightColorText by UIText("Line of Sight Color") constrain {
            x = lineOfSightColorTextX ?: run { lineOfSightColorTextX = (eyelineColorText.getRight() + 5).pixels(); lineOfSightColorTextX!! }
            y = 0.pixels()
        } childOf (colorContainer)
        val lineOfSightColorPicker by ColorComponent(Color(Entity.map[if (i == 0) 1 else i]!!.lineColor), false) constrain {
            x = lineOfSightColorX ?: run {
                lineOfSightColorX = (lineOfSightColorText.getLeft() - 34).pixels(); lineOfSightColorX!!
            }
            y = (CopyConstraintFloat(true) boundTo lineOfSightColorText) + 10.pixels()
        } childOf (colorContainer)
        lineOfSightColorPicker.onValueChange { value ->
            if (i == 0) {
                Entity.map.forEach {
                    it.value.lineColor = (value as Color).rgb
                }
            } else {
                Entity.map[i]!!.lineColor = (value as Color).rgb
            }
            Hitboxes.writeConfig()
        }
    }

    override fun onScreenClose() {
        super.onScreenClose()
        entityToEmulate = Entity.blank
        if (returnToConfigGUI) {
            EssentialAPI.getGuiUtil().openScreen(RedactionConfig.gui())
        }
        Hitboxes.writeConfig()
    }

    companion object {
        val bypassHitbox
        get() = Minecraft.getMinecraft().currentScreen is HitboxPreviewGUI

        var entityToEmulate: Entity = Entity.blank
            private set
    }
}
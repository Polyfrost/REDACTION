package xyz.qalcyo.redaction.hud.elements

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.buildEmulatedPlayer
import gg.essential.elementa.UIComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.Window
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.gui.inventory.GuiInventory
import xyz.qalcyo.redaction.Redaction.mc
import xyz.qalcyo.redaction.hud.Element
import java.awt.Color

class PlayerInfo: Element() {
    private var window: Window = Window(30) constrain {
        x = 10.pixels()
        y = 10.pixels()
        width = ChildBasedSizeConstraint()
        height = ChildBasedSizeConstraint()
    }
    private var player: UIComponent = object: UIComponent() {

    } constrain {
        x = window.constraints.x
        y = window.constraints.y
        width = 10.percent()
        height = 10.percent()
    } childOf window
    private var background: UIBlock = UIBlock(Color(0, 0, 0, 50)) constrain {
        x = window.constraints.x
        y = window.constraints.y
        width = window.constraints.width
        height = window.constraints.height
    } childOf window
    private var firstTime = true

    override fun render(res: ScaledResolution, partialTicks: Float) {
        if (firstTime) {
            player = EssentialAPI.getEssentialComponentFactory().buildEmulatedPlayer {
                this.profileState.set(mc.thePlayer.gameProfile)
                this.showCapeState.set(false)
            }
            firstTime = false
        }
        if (mc.currentScreen !is GuiInventory) {
            window.draw()
        }
    }
}
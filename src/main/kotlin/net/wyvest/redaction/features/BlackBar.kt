package net.wyvest.redaction.features

import gg.essential.api.utils.Multithreading
import gg.essential.universal.UResolution
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.utils.MathUtil
import net.wyvest.redaction.utils.RenderUtils
import java.awt.event.ActionListener
import javax.swing.Timer

object BlackBar {

    private var cachedHeight = 0
    private var cachedWidth = 0
    private data class BlackBarData constructor(var x: Float, var y: Int, var hiding: Boolean = false, var lastSlot: Int = 10)
    private var data: BlackBarData? = null
    private val texPath = ResourceLocation("textures/gui/widgets.png")
    private var firstTime = true
    private var entityplayer: EntityPlayer? = null
    private var timer: Timer? = null

    private val timerTask: ActionListener = ActionListener {
        if (cachedHeight != UResolution.scaledHeight || cachedWidth != UResolution.scaledWidth) {
            data = BlackBarData(-1.0F, UResolution.scaledHeight - 22)
            cachedHeight = UResolution.scaledHeight
            cachedWidth = UResolution.scaledWidth
        }
        if (entityplayer != null && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null) {
            data?.let {
                val scaledHeight = UResolution.scaledHeight
                val scaledWidth = UResolution.scaledWidth
                it.hiding = Minecraft.getMinecraft().currentScreen is GuiChat
                if (it.hiding) {
                    it.y = MathUtil.lerp(it.y.toFloat(), scaledHeight.toFloat() + 2, Minecraft.getMinecraft().timer.renderPartialTicks / 4).toInt()
                } else if (it.y != scaledHeight - 21) {
                    it.y = MathUtil.lerp(it.y.toFloat(), scaledHeight.toFloat() - 22, Minecraft.getMinecraft().timer.renderPartialTicks / 4).toInt()
                }
                if (it.lastSlot != entityplayer!!.inventory.currentItem) {
                    if (scaledWidth / 2 - 91F + entityplayer!!.inventory.currentItem * 20 != it.x) {
                        it.x = MathUtil.lerp(
                            it.x,
                            scaledWidth / 2 - 91F + entityplayer!!.inventory.currentItem * 20,
                            Minecraft.getMinecraft().timer.renderPartialTicks / 4
                        )
                    } else {
                        it.lastSlot = entityplayer!!.inventory.currentItem
                    }
                } else {
                    it.lastSlot = entityplayer!!.inventory.currentItem
                    it.x = scaledWidth / 2 - 91F + entityplayer!!.inventory.currentItem * 20
                }
            }
        }
    }

    fun initialize() {
        setTimer()
    }

    fun setTimer() {
        Multithreading.runAsync {
            timer?.stop()
            timer = Timer(RedactionConfig.blackbarSpeed, timerTask)
            timer?.start()
        }
    }

    fun render() {
        data?.let {
            entityplayer = Minecraft.getMinecraft().renderViewEntity as EntityPlayer
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            Minecraft.getMinecraft().textureManager.bindTexture(texPath)
            if (firstTime) {
                firstTime = false
                it.x = 0F
                it.y = UResolution.scaledHeight - 22
                it.hiding = false
            }
            if (RedactionConfig.blackbarColor.alpha != 0) {
                RenderUtils.drawRectEnhanced(0, it.y, UResolution.scaledWidth, 22, RedactionConfig.blackbarColor.rgb)
            }
            if (RedactionConfig.blackbarItemColor.alpha != 0) {
                RenderUtils.drawRectangle(
                    it.x,
                    it.y.toFloat(), 22F, 22F, RedactionConfig.blackbarItemColor
                )
            }
        }
    }
}
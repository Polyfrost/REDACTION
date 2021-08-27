package net.wyvest.redaction.hud.elements

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.hud.Element
import net.wyvest.redaction.utils.GlUtil
import net.wyvest.redaction.utils.MathUtil

class BlackBar : Element() {

    private var data: BlackBarData? = null
    private val texPath = ResourceLocation("textures/gui/widgets.png")
    private var firstTime = true


    override fun initialize() {
        val resolution = ScaledResolution(Minecraft.getMinecraft())
        data = BlackBarData(-1.0F, resolution.scaledHeight - 22)
    }

    override fun render(res: ScaledResolution,
                        partialTicks: Float) {
        data?.let {
            if (Redaction.mc.renderViewEntity is EntityPlayer) {
                val entityplayer = Redaction.mc.renderViewEntity as EntityPlayer
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
                Redaction.mc.textureManager.bindTexture(texPath)
                val scaledWidth = res.scaledWidth
                val scaledHeight = res.scaledHeight
                it.hiding = Minecraft.getMinecraft().currentScreen is GuiChat
                if (firstTime) {
                    firstTime = false
                    it.x = 0F
                    it.y = scaledHeight - 22
                    it.hiding = false
                }
                if (it.hiding) {
                    it.y = MathUtil.lerp(it.y.toFloat(), scaledHeight.toFloat() + 2, partialTicks / 4).toInt()
                } else if (!it.hiding && it.y != scaledHeight - 22) {
                    it.y = MathUtil.lerp(it.y.toFloat(), scaledHeight.toFloat() - 22, partialTicks / 4).toInt()
                }
                if (it.lastSlot != entityplayer.inventory.currentItem) {
                    if (scaledWidth / 2 - 90.5F + entityplayer.inventory.currentItem * 20 != it.x) {
                        it.x = MathUtil.lerp(it.x, scaledWidth / 2 - 90.5F + entityplayer.inventory.currentItem * 20, partialTicks / 4)
                    } else {
                        it.lastSlot = entityplayer.inventory.currentItem
                    }
                } else {
                    it.lastSlot = entityplayer.inventory.currentItem
                    it.x = scaledWidth / 2 - 90.5F + entityplayer.inventory.currentItem * 20
                }
                if (RedactionConfig.blackbarColor.alpha != 0) {
                    GlUtil.drawRectEnhanced(0, it.y, scaledWidth, 22, RedactionConfig.blackbarColor.rgb)
                }
                if (RedactionConfig.blackbarItemColor.alpha != 0) {
                    GlUtil.drawRectangle(it.x,
                        it.y.toFloat(), 22F, 22F, RedactionConfig.blackbarItemColor)
                }
            }
        }
    }

    override fun actuallyRender(): Boolean {
        return false
    }
}

private class BlackBarData(
    var x: Float,
    var y: Int
) {
    var hiding = false
    var lastSlot = 10
}
package net.wyvest.redaction.blackbar

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.wyvest.redaction.Redaction.mc
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.utils.GlUtil
import xyz.matthewtgm.requisite.util.MathHelper
import xyz.matthewtgm.requisite.util.RenderHelper

object BlackBarManager {

    private var data: BlackBarData? = null
    private val texPath = ResourceLocation("textures/gui/widgets.png")
    private var firstTime = true

    @SubscribeEvent
    fun initialize(event: WorldEvent.Load) {
        val resolution = ScaledResolution(Minecraft.getMinecraft())
        data = BlackBarData(-1.0F, resolution.scaledHeight - 22)
    }

    fun render(
        res: ScaledResolution,
        partialTicks: Float
    ) {
        data?.let {
            if (mc.renderViewEntity is EntityPlayer) {
                val entityplayer = mc.renderViewEntity as EntityPlayer
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
                mc.textureManager.bindTexture(texPath)
                val scaledWidth = res.scaledWidth
                val scaledHeight = res.scaledHeight
                it.hiding = Minecraft.getMinecraft().currentScreen is GuiChat
                if (firstTime) {
                    firstTime = false
                    it.x = 0F
                    it.y = scaledHeight - 22
                    it.hiding = false
                }
                if (it.hiding && RedactionConfig.hideBlackbar) {
                    it.y = MathHelper.lerp(it.y.toFloat(), scaledHeight.toFloat() + 2, partialTicks / 4).toInt()
                } else if (!it.hiding && it.y != scaledHeight - 22) {
                    it.y = MathHelper.lerp(it.y.toFloat(), scaledHeight.toFloat() - 22, partialTicks / 4).toInt()
                }
                if (it.lastSlot != entityplayer.inventory.currentItem) {
                    if (scaledWidth / 2 - 90.5F + entityplayer.inventory.currentItem * 20 != it.x) {
                        it.x = MathHelper.lerp(it.x, scaledWidth / 2 - 90.5F + entityplayer.inventory.currentItem * 20, partialTicks / 4)
                    } else {
                        it.lastSlot = entityplayer.inventory.currentItem
                    }
                } else {
                    it.lastSlot = entityplayer.inventory.currentItem
                    it.x = scaledWidth / 2 - 90.5F + entityplayer.inventory.currentItem * 20
                }
                if (RedactionConfig.blackbarColor.alpha != 0) {
                    RenderHelper.drawRectEnhanced(0, it.y, scaledWidth, 22, RedactionConfig.blackbarColor.rgb)
                }
                if (RedactionConfig.blackbarItemColor.alpha != 0) {
                    GlUtil.drawRectangle(it.x,
                        it.y.toFloat(), 22F, 22F, RedactionConfig.blackbarItemColor)
                }
                if(RedactionConfig.blackbarSecondColor && RedactionConfig.blackbarItemColor2.alpha != 0) {
                    GlUtil.drawRectangle(it.x + 2,
                        it.y.toFloat() + 2F, 18F, 18F, RedactionConfig.blackbarItemColor2)
                }
            }
        }
    }

}

private class BlackBarData(
    var x: Float,
    var y: Int
) {
    var hiding = false
    var lastSlot = 10
}
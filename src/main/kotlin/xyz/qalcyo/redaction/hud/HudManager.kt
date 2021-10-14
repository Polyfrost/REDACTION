package xyz.qalcyo.redaction.hud

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import xyz.qalcyo.redaction.Redaction.mc
import xyz.qalcyo.redaction.hud.elements.BlackBar
import xyz.qalcyo.redaction.hud.elements.PlayerInfo
import xyz.qalcyo.redaction.render.ScreenRenderer
import java.awt.Color

object HudManager {

    val elements = listOf(
        BlackBar(),
        PlayerInfo()
    )

    fun initialize() {
        for (e in elements) {
            e.initialize()
        }
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun render(event: RenderGameOverlayEvent.Post) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            if (mc.thePlayer != null) {
                ScreenRenderer.isRendering = true
                ScreenRenderer.drawString("§rhi", 30F, 30F, Color.WHITE.rgb, true)
                ScreenRenderer.drawString("§ahi§ra", 30F, 70F, Color.WHITE.rgb, true)
                for (e in elements) {
                    if (e.actuallyRender()) {
                        e.render(event.resolution, event.partialTicks)
                    }
                }
                ScreenRenderer.isRendering = false
            }
        }
    }

}
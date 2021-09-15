package xyz.qalcyo.redaction.hud

import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import xyz.qalcyo.redaction.Redaction.mc
import xyz.qalcyo.redaction.hud.elements.BlackBar

object HudManager {

    val elements = listOf(
        BlackBar()
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
                for (e in elements) {
                    if (e.actuallyRender()) {
                        e.render(event.resolution, event.partialTicks)
                    }
                }
            }
        }
    }

}
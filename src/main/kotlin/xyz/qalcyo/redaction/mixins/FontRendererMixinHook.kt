package xyz.qalcyo.redaction.mixins

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import xyz.qalcyo.redaction.Redaction.mc
import xyz.qalcyo.redaction.render.ScreenRenderer

object FontRendererMixinHook {
    fun modifyText(text: String?, x: Float, y: Float, color: Int, dropShadow: Boolean,
                   cir: CallbackInfoReturnable<Int>) {
        if (text != null && mc.thePlayer != null) {
            if (text.contains(mc.thePlayer.name, true)) {
                cir.cancel()
                ScreenRenderer.isRendering = true
                ScreenRenderer.drawString(text.replace(mc.thePlayer.name, "§w${mc.thePlayer.name}§r"), x, y, color, dropShadow)
                ScreenRenderer.isRendering = false
            }
        }
    }
}
package xyz.qalcyo.redaction.mixins

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import xyz.qalcyo.redaction.render.ScreenRenderer

object FontRendererMixinHook {
    fun modifyText(text: String?, x: Float, y: Float, color: Int, dropShadow: Boolean,
                   cir: CallbackInfoReturnable<Int>) {
        if (text != null) {
            if (text.contains("Wyvest")) {
                cir.cancel()
                ScreenRenderer.isRendering = true
                ScreenRenderer.drawString(text.replace("Wyvest", "§wWyvest§r"), x, y, color, dropShadow)
                ScreenRenderer.isRendering = false
            }
        }
    }
}
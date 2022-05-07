package net.wyvest.redaction.mixin;

import club.sk1er.patcher.mixins.accessors.FontRendererAccessor;
import club.sk1er.patcher.util.enhancement.text.CachedString;
import net.minecraft.client.renderer.GlStateManager;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "club.sk1er.patcher.hooks.FontRendererHook", remap = false)
public class PatcherFontRendererMixin {
    @Shadow @Final private FontRendererAccessor fontRendererAccessor;
    private CachedString cachedString = null;
    private Character charCaptured = null;

    @Dynamic("Patcher")
    @ModifyVariable(method = "renderStringAtPos", at = @At("STORE"), index = 15)
    private CachedString captureCachedString(CachedString value) {
        cachedString = value;
        return cachedString;
    }

    @Dynamic("Patcher")
    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    private char captureChar(String instance, int index) {
        char c = instance.charAt(index);
        charCaptured = c;
        return c;
    }

    @Dynamic("Patcher")
    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lclub/sk1er/patcher/util/enhancement/text/CachedString;setLastRed(F)V", ordinal = 2, shift = At.Shift.BY, by = 2))
    private void onStringRendered_setColor(String text, boolean shadow, CallbackInfoReturnable<Boolean> cir) {
        if (charCaptured != null && cachedString != null) {
            if (charCaptured == 'w') {
                float r = RedactionConfig.INSTANCE.getRedText(shadow);
                float g = RedactionConfig.INSTANCE.getGreenText(shadow);
                float b = RedactionConfig.INSTANCE.getBlueText(shadow);
                float a = fontRendererAccessor.getAlpha();
                this.fontRendererAccessor.setTextColor(getRGBA((int) r, (int) g, (int) b, (int) (a * 255)));
                GlStateManager.color(r / 255f, g / 255f, b / 255f, a);

                cachedString.setLastAlpha(a);
                cachedString.setLastGreen(g / 255f);
                cachedString.setLastBlue(b / 255f);
                cachedString.setLastRed(r / 255f);
            }
        }
        charCaptured = null;
    }

    @Dynamic("Patcher")
    @Inject(method = "renderStringAtPos", at = @At("RETURN"))
    private void resetCachedString(String text, boolean shadow, CallbackInfoReturnable<Boolean> cir) {
        cachedString = null;
    }

    private int getRGBA(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF));
    }
}

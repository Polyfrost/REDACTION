package net.wyvest.redaction.mixin;

import net.minecraft.client.gui.FontRenderer;
import net.wyvest.redaction.config.RedactionConfig;
import net.wyvest.redaction.features.NameHighlight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @Shadow protected abstract void setColor(float r, float g, float b, float a);

    @Shadow private int textColor;
    @Shadow private float alpha;
    private Character charCaptured = null;
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String onStringRendered_modifyText(String original) {
        return (RedactionConfig.INSTANCE.getHighlightName() && original != null) ? NameHighlight.highlightName(original) : original;
    }

    @Redirect(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 1))
    private char captureChar(String instance, int index) {
        char c = instance.charAt(index);
        charCaptured = c;
        return c;
    }

    @Inject(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;setColor(FFFF)V", ordinal = 1, shift = At.Shift.BY, by = 2))
    private void onStringRendered_setColor(String text, boolean shadow, CallbackInfo ci) {
        if (charCaptured != null) {
            if (charCaptured == 'w') {
                float r = RedactionConfig.INSTANCE.getRedText(shadow);
                float g = RedactionConfig.INSTANCE.getGreenText(shadow);
                float b = RedactionConfig.INSTANCE.getBlueText(shadow);
                textColor = getRGBA((int) r, (int) g, (int) b, (int) (alpha * 255));
                setColor(r / 255f, g / 255f, b / 255f, alpha);
            }
        }
        charCaptured = null;
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String onStringWidth_modifyText(String original) {
        return (RedactionConfig.INSTANCE.getHighlightName() && original != null) ? NameHighlight.highlightName(original) : original;
    }

    private int getRGBA(int r, int g, int b, int a) {
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8)  |
                ((b & 0xFF));
    }
}

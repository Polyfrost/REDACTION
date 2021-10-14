package xyz.qalcyo.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.qalcyo.redaction.Redaction;
import xyz.qalcyo.redaction.config.RedactionConfig;
import xyz.qalcyo.redaction.mixins.FontRendererMixinHook;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {

    @Inject(method = "renderString", at = @At("HEAD"), cancellable = true)
    public void renderString_modifyText(String text, float x, float y, int color, boolean dropShadow, CallbackInfoReturnable<Integer> cir) {
        FontRendererMixinHook.INSTANCE.modifyText(text, x, y, color, dropShadow, cir);
    }

    @ModifyVariable(method = "drawString(Ljava/lang/String;FFIZ)I", at = @At("HEAD"), index = 5)
    private boolean overrideShadow(boolean dropShadow) {
        if (RedactionConfig.INSTANCE.getForceShadow() && Redaction.INSTANCE.isMainMenu() && (!RedactionConfig.INSTANCE.getGuiOpenShadow() || Minecraft.getMinecraft().currentScreen == null)) {
            return true;
        } else {
            return dropShadow;
        }
    }

    @ModifyArg(method = "drawString(Ljava/lang/String;III)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;FFIZ)I"), index = 4)
    private boolean overrideShadowB(boolean dropShadow) {
        if (RedactionConfig.INSTANCE.getForceShadow() && Redaction.INSTANCE.isMainMenu() && (!RedactionConfig.INSTANCE.getGuiOpenShadow() || Minecraft.getMinecraft().currentScreen == null)) {
            return true;
        } else {
            return dropShadow;
        }
    }
}

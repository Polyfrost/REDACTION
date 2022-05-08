package net.wyvest.redaction.mixin;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.model.IBakedModel;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "cc.woverflow.overflowanimations.GlintHandler", remap = false)
public class OverflowGlintHandlerMixin {
    @Inject(method = "renderGlint", at = @At("RETURN"))
    private void afterRenderEffect(IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.disableStandardItemLighting();
        }
    }
}

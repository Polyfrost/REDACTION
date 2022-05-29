package net.wyvest.redaction.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderEffect", at = @At("HEAD"))
    private void beforeRenderEffect(IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Inject(method = "renderEffect", at = @At("TAIL"))
    private void afterRenderEffect(IBakedModel model, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.disableStandardItemLighting();
        }
    }
}

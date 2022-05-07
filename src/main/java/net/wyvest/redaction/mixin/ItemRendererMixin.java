package net.wyvest.redaction.mixin;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Inject(method = "setLightMapFromPlayer", at = @At("HEAD"), cancellable = true)
    private void cancelLightMap(AbstractClientPlayer clientPlayer, CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getDisableHandLighting()) {
            ci.cancel();
        }
    }

    @Redirect(method = "rotateArroundXAndY", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;enableStandardItemLighting()V"))
    private void cancelLighting() {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.enableStandardItemLighting();
        }
    }

    @Redirect(method = "renderItemInFirstPerson", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    private void resetLighting() {
        if (!RedactionConfig.INSTANCE.getDisableHandLighting()) {
            RenderHelper.disableStandardItemLighting();
        }
    }
}

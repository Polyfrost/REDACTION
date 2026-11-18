package org.polyfrost.redaction.mixin.client.legacy;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.entity.living.player.ClientPlayerEntity;
import net.minecraft.client.render.ItemInHandRenderer;
import org.polyfrost.redaction.client.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin_DisableHandLighting {
    @Inject(method = "setHandLightColor", at = @At("HEAD"), cancellable = true)
    private void skipHandLightColor(ClientPlayerEntity player, CallbackInfo ci) {
        if (redaction$isEnabled()) {
            ci.cancel();
        }
    }

    @WrapWithCondition(method = "rotate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/Lighting;turnOn()V"))
    private boolean skipItemLighting() {
        return !redaction$isEnabled();
    }

    @Unique
    private static boolean redaction$isEnabled() {
        return RedactionConfig.INSTANCE.getModEnabled() && RedactionConfig.INSTANCE.getDisableHandLighting();
    }
}

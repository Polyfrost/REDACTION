package net.wyvest.redaction.mixin;

import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.wyvest.redaction.features.particles.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EffectRenderer.class)
public class EffectRendererMixin {
    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    private void cancelEffect(EntityFX effect, CallbackInfo ci) {
        for (Particle particle : Particle.values) {
            if (particle.getCondition().invoke(effect)) {
                if (particle.getEnabled()) {
                    ci.cancel();
                }
                break;
            }
        }
    }
}

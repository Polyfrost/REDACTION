package net.wyvest.redaction.mixin;

import cc.woverflow.onecore.utils.ColorUtils;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.wyvest.redaction.features.particles.Particle;
import net.wyvest.redaction.features.particles.Particles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EffectRenderer.class)
public class EffectRendererMixin {
    @Inject(method = "addEffect", at = @At("HEAD"), cancellable = true)
    private void cancelEffect(EntityFX effect, CallbackInfo ci) {
        for (Particle particle : Particle.values) {
            if (particle.getCondition().invoke(effect)) {
                if (!particle.getEnabled()) {
                    ci.cancel();
                }
                break;
            }
        }
    }

    @Redirect(method = {"renderParticles", "renderLitParticles"}, at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private <E> E setRenderingParticles(List<E> instance, int i) {
        E e = instance.get(i);
        if (e instanceof EntityFX) {
            for (Particle particle : Particle.values) {
                if (particle.getCondition().invoke((EntityFX) e)) {
                    Particles.INSTANCE.setCurrentRenderingParticle(particle);
                    break;
                }
            }
        }
        return e;
    }

    @Redirect(method = {"renderParticles", "renderLitParticles"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/EntityFX;renderParticle(Lnet/minecraft/client/renderer/WorldRenderer;Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void wrapRenderParticle(EntityFX instance, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        try {
            if (Particles.INSTANCE.getCurrentRenderingParticle() != null) {
                GlStateManager.scale(Particles.INSTANCE.getCurrentRenderingParticle().getScale(), Particles.INSTANCE.getCurrentRenderingParticle().getScale(), 1.0F);
            }
            instance.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
            Particles.INSTANCE.setCurrentRenderingParticle(null);
        } catch (Exception e) {
            Particles.INSTANCE.setCurrentRenderingParticle(null);
            throw e;
        }
    }
}

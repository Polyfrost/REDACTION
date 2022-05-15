package net.wyvest.redaction.mixin;

import cc.woverflow.onecore.utils.ColorUtils;
import net.minecraft.client.renderer.WorldRenderer;
import net.wyvest.redaction.features.particles.Particles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @ModifyVariable(method = "color(FFFF)Lnet/minecraft/client/renderer/WorldRenderer;", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private float changeRed(float value) {
        return Particles.INSTANCE.getCurrentRenderingParticle() != null ? ColorUtils.getRed(Particles.INSTANCE.getCurrentRenderingParticle().getTint()) : value;
    }

    @ModifyVariable(method = "color(FFFF)Lnet/minecraft/client/renderer/WorldRenderer;", at = @At(value = "HEAD"), ordinal = 1, argsOnly = true)
    private float changeGreen(float value) {
        return Particles.INSTANCE.getCurrentRenderingParticle() != null ? ColorUtils.getGreen(Particles.INSTANCE.getCurrentRenderingParticle().getTint()) : value;
    }

    @ModifyVariable(method = "color(FFFF)Lnet/minecraft/client/renderer/WorldRenderer;", at = @At(value = "HEAD"), ordinal = 2, argsOnly = true)
    private float changeBlue(float value) {
        return Particles.INSTANCE.getCurrentRenderingParticle() != null ? ColorUtils.getBlue(Particles.INSTANCE.getCurrentRenderingParticle().getTint()) : value;
    }
}

package net.wyvest.redaction.mixin;

import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityFX.class)
public interface EntityFXAccessor {
    @Accessor("particleTextureIndexX")
    int getParticleTextureX();
}

package net.wyvest.redaction.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EntityDropParticleFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityDropParticleFX.class)
public interface EntityDropParticleFXAccessor {
    @Accessor("materialType")
    Material getMaterial();
}

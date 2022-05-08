package net.wyvest.redaction.mixin;

import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.wyvest.redaction.hook.EntityBreakingFXHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityBreakingFX.class)
public class EntityBreakingFXMixin implements EntityBreakingFXHook {
    private Item item = null;
    private boolean isProjectile;

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/Item;)V", at = @At("RETURN"))
    private void markAsSnowball(World world, double d, double e, double f, Item item, CallbackInfo ci) {
        isProjectile = true;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/Item;I)V", at = @At("RETURN"))
    private void getItem(World world, double d, double e, double f, Item item, int i, CallbackInfo ci) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public boolean isProjectile() {
        return isProjectile;
    }
}

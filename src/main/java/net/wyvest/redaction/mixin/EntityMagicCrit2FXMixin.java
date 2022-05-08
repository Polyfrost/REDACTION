package net.wyvest.redaction.mixin;

import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityFX;
import net.wyvest.redaction.hook.EntityCrit2FXHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityCrit2FX.class)
public class EntityMagicCrit2FXMixin implements EntityCrit2FXHook {
    private boolean isMagic = false;
    @Override
    public boolean isMagic() {
        return isMagic;
    }

    @Override
    public void setMagic(boolean value) {
        isMagic = value;
    }

    @Mixin(targets = "net.minecraft.client.particle.EntityCrit2FX$MagicFactory")
    public static class MagicFactoryMixin {
        @Redirect(method = "getEntityFX", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/EntityFX;setRBGColorF(FFF)V"))
        private void captureEntityFX(EntityFX instance, float particleRedIn, float particleGreenIn, float particleBlueIn) {
            instance.setRBGColorF(particleRedIn, particleGreenIn, particleBlueIn);
            ((EntityCrit2FXHook) instance).setMagic(true);
        }
    }
}

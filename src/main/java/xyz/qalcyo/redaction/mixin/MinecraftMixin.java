package xyz.qalcyo.redaction.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.qalcyo.redaction.Redaction;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "startGame", at = @At("TAIL"))
    private void a(CallbackInfo ci) {
        Redaction.INSTANCE.setMainMenu(true);
    }
}

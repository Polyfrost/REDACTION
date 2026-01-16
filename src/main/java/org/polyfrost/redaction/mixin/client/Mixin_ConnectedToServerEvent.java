package org.polyfrost.redaction.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.polyfrost.oneconfig.api.event.v1.EventManager;
import org.polyfrost.redaction.client.events.ConnectedToServerEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class Mixin_ConnectedToServerEvent {
    @Inject(method = "handleLogin", at = @At(value = "TAIL"))
    private void onLogin(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {
        EventManager.INSTANCE.post(ConnectedToServerEvent.INSTANCE);
    }
}

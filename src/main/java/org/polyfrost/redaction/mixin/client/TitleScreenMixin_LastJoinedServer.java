package org.polyfrost.redaction.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.ServerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//~ if <1.21.4 'CallbackInfoReturnable' -> 'CallbackInfo'
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
abstract class TitleScreenMixin_LastJoinedServer extends Screen {
    protected TitleScreenMixin_LastJoinedServer(Component title) {
        super(title);
    }

    @ModifyArg(
            method = "createNormalMenuOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;",
                    //~ if <26.2 '2' -> '1'
                    ordinal = 2
            ),
            index = 2
    )
    private int modifyMultiplayerWidth(int i, @Local Component multiplayerDisabledReason) {
        if (multiplayerDisabledReason != null) return i;

        String ip = RedactionConfig.INSTANCE.getLastServerIP();
        if (RedactionConfig.INSTANCE.getLastServerJoined() && !ip.trim().isEmpty()) {
            return 98;
        }

        return i;
    }

    @Inject(
            method = "createNormalMenuOptions",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/TitleScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;",
                    //~ if <26.2 '2' -> '1'
                    ordinal = 2
            )
    )
    //~ if <1.21.4 'CallbackInfoReturnable<Integer>' -> 'CallbackInfo'
    private void addLastServerButton(int topPos, int spacing, CallbackInfoReturnable<Integer> cir, @Local Component multiplayerDisabledReason) {
        if (multiplayerDisabledReason != null) return;

        String ip = RedactionConfig.INSTANCE.getLastServerIP();
        if (RedactionConfig.INSTANCE.getLastServerJoined() && !ip.trim().isEmpty()) {
            this.addRenderableWidget(Button.builder(Component.literal(ip), (button) -> {
                ConnectScreen.startConnecting(
                        this,
                        this.minecraft,
                        ServerAddress.parseString(ip),
                        new ServerData(ServerManager.getServerName(ip), ip, ServerData.Type.OTHER), false, null
                );
            }).bounds(this.width / 2 + 2, this.height / 4 + 48 + spacing, 98, 20).build());
        }
    }
}

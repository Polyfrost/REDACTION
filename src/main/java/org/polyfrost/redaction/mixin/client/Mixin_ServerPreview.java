package org.polyfrost.redaction.mixin.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.ServerManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectJoinServerScreen.class)
public class Mixin_ServerPreview extends Screen {
    @Shadow @Final private Screen lastScreen;
    @Shadow private EditBox ipEdit;

    @Unique private ServerData redaction$serverPreview;
    @Unique private ServerSelectionList.OnlineServerEntry redaction$serverEntry;

    protected Mixin_ServerPreview(Component component) {
        super(component);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initServerPreview(CallbackInfo ci) {
        if (RedactionConfig.INSTANCE.getServerPreview()) {
            this.redaction$serverPreview = new ServerData("", "", ServerData.Type.OTHER);
            if (this.lastScreen instanceof JoinMultiplayerScreen joinMultiplayerScreen) {
                this.redaction$serverEntry = Mixin_OnlineServerEntryAccessor.create(
                        ((Mixin_JoinMultiplayerScreenAccessor) joinMultiplayerScreen).getServerSelectionList(),
                        joinMultiplayerScreen,
                        this.redaction$serverPreview
                );
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void drawServerPreview(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (!RedactionConfig.INSTANCE.getServerPreview() || redaction$serverPreview == null || redaction$serverEntry == null) {
            return;
        }

        if (!this.redaction$serverPreview.ip.equals(this.ipEdit.getValue())) {
            this.redaction$serverPreview = new ServerData(
                    ServerManager.getServerName(this.ipEdit.getValue()), this.ipEdit.getValue(), ServerData.Type.OTHER
            );

            JoinMultiplayerScreen joinMultiplayerScreen = (JoinMultiplayerScreen) this.lastScreen;
            this.redaction$serverEntry = Mixin_OnlineServerEntryAccessor.create(
                    ((Mixin_JoinMultiplayerScreenAccessor) joinMultiplayerScreen).getServerSelectionList(),
                    joinMultiplayerScreen,
                    this.redaction$serverPreview
            );
        }

        //? if >=1.21.10 {
        redaction$serverEntry.setX(guiGraphics.guiWidth() / 2 - 100);
        redaction$serverEntry.setY(30);
        redaction$serverEntry.setWidth(200);
        redaction$serverEntry.setHeight(35);

        redaction$serverEntry.renderContent(
                guiGraphics,
                i,
                j,
                false,
                f
        );
        //?} else {
        /*redaction$serverEntry.render(
                guiGraphics,
                0,
                30,
                guiGraphics.guiWidth() / 2 - 100,
                200,
                35,
                i,
                j,
                false,
                f
        );
        *///?}
    }
}
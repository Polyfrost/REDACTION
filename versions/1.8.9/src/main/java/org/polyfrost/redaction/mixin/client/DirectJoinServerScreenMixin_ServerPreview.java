package org.polyfrost.redaction.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import org.polyfrost.redaction.client.RedactionConfig;
import org.polyfrost.redaction.client.features.ServerManager;
import org.polyfrost.redaction.mixin.client.accessor.JoinMultiplayerScreenAccessor;
import org.polyfrost.redaction.mixin.client.accessor.OnlineServerEntryAccessor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DirectJoinServerScreen.class)
abstract class DirectJoinServerScreenMixin_ServerPreview extends Screen {
    @Shadow @Final private Screen lastScreen;
    @Shadow private EditBox ipEdit;

    @Unique private ServerData redaction$serverData;
    @Unique private ServerSelectionList.OnlineServerEntry redaction$serverPreview;
    @Unique private boolean redaction$initialized = false;
    @Unique private long redaction$lastInputTime;

    protected DirectJoinServerScreenMixin_ServerPreview(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void initServerPreview(CallbackInfo ci) {
        if (!RedactionConfig.INSTANCE.getServerPreview()) return;

        if (!redaction$initialized) {
            createServerPreview();
            redaction$initialized = true;
        }

        setServerPreviewDimensions();
    }

    //~ if <26.1 'extractRenderState' -> 'render'
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void drawServerPreview(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (!RedactionConfig.INSTANCE.getServerPreview() || redaction$serverData == null || redaction$serverPreview == null) {
            return;
        }

        if (System.currentTimeMillis() - redaction$lastInputTime > 300 && !redaction$serverData.ip.equals(this.ipEdit.getValue())) {
            createServerPreview();
        }

        //? if >=26.1 {
        redaction$serverPreview.extractContent(
        //?} elif >=1.21.10 {
        /*redaction$serverPreview.renderContent(
        *///?} else
        //redaction$serverPreview.render(
                graphics,
                //? if <1.21.10 {
                /*0,
                50,
                graphics.guiWidth() / 2 - 305 / 2,
                305,
                35,
                *///?}
                mouseX,
                mouseY,
                false,
                a
        );
    }

    @Inject(method = "updateSelectButtonStatus", at = @At("TAIL"))
    private void updateServerPreview(CallbackInfo ci) {
        redaction$lastInputTime = System.currentTimeMillis();
    }

    @Unique
    private void createServerPreview() {
        String ip = this.ipEdit.getValue();
        String name = !ip.isEmpty() ? ServerManager.getServerName(ip) : "Server Preview";
        redaction$serverData = new ServerData(
                name,
                ip,
                ServerData.Type.OTHER
        );

        JoinMultiplayerScreen screen;
        if (this.lastScreen instanceof JoinMultiplayerScreen) {
            screen = (JoinMultiplayerScreen) this.lastScreen;
        } else {
            screen = new JoinMultiplayerScreen(this.lastScreen);
        }

        redaction$serverPreview = OnlineServerEntryAccessor.create(
                ((JoinMultiplayerScreenAccessor) screen).getServerSelectionList(),
                screen,
                redaction$serverData
        );
        setServerPreviewDimensions();
    }

    @Unique
    private void setServerPreviewDimensions() {
        //? if >=1.21.10 {
        redaction$serverPreview.setWidth(305);
        redaction$serverPreview.setHeight(35);
        redaction$serverPreview.setX(this.width / 2 - 305 / 2);
        redaction$serverPreview.setY(50);
        //?}
    }
}

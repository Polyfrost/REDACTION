package net.wyvest.redaction.mixin;

import gg.essential.elementa.state.BasicState;
import net.wyvest.redaction.config.VigilanceConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Pseudo
@Mixin(targets = "gg.essential.vigilance.gui.VigilancePalette", remap = false)
public class VigilancePaletteMixin {

    @Inject(method = "getBrightDivider", at = @At("HEAD"), cancellable = true)
    public void getBrightDivider(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getBrightDivider());
    }

    @Inject(method = "getDivider", at = @At("HEAD"), cancellable = true)
    public void getDivider(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getDivider());
    }

    @Inject(method = "getDarkDivider", at = @At("HEAD"), cancellable = true)
    public void getDarkDivider(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getDarkDivider());
    }

    @Inject(method = "getOutline", at = @At("HEAD"), cancellable = true)
    public void getOutline(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getOutline());
    }

    @Inject(method = "getScrollBar", at = @At("HEAD"), cancellable = true)
    public void getScrollBar(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getScrollBar());
    }

    @Inject(method = "getBrightHighlight", at = @At("HEAD"), cancellable = true)
    public void getBrightHighlight(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getBrightHighlight());
    }

    @Inject(method = "getHighlight", at = @At("HEAD"), cancellable = true)
    public void getHighlight(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getHighlight());
    }

    @Inject(method = "getDarkHighlight", at = @At("HEAD"), cancellable = true)
    public void getDarkHighlight(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getDarkHighlight());
    }

    @Inject(method = "getLightBackground", at = @At("HEAD"), cancellable = true)
    public void getLightBackground(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getLightBackground());
    }

    @Inject(method = "getBackground", at = @At("HEAD"), cancellable = true)
    public void getBackground(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getBackground());
    }

    @Inject(method = "getDarkBackground", at = @At("HEAD"), cancellable = true)
    public void getDarkBackground(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getDarkBackground());
    }

    @Inject(method = "getSearchBarBackground", at = @At("HEAD"), cancellable = true)
    public void getSearchBarBackground(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getSearchBarBackground());
    }

    @Inject(method = "getBrightText", at = @At("HEAD"), cancellable = true)
    public void getBrightText(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getBrightText());
    }

    @Inject(method = "getMidText", at = @At("HEAD"), cancellable = true)
    public void getMidText(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getMidText());
    }

    @Inject(method = "getDarkText", at = @At("HEAD"), cancellable = true)
    public void getDarkText(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getDarkText());
    }

    @Inject(method = "getModalBackground", at = @At("HEAD"), cancellable = true)
    public void getModalBackground(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getModalBackground());
    }

    @Inject(method = "getWarning", at = @At("HEAD"), cancellable = true)
    public void getWarning(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getWarning());
    }

    @Inject(method = "getAccent", at = @At("HEAD"), cancellable = true)
    public void getAccent(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getAccent());
    }

    @Inject(method = "getSuccess", at = @At("HEAD"), cancellable = true)
    public void getSuccess(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getSuccess());
    }

    @Inject(method = "getTransparent", at = @At("HEAD"), cancellable = true)
    public void getTransparent(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getTransparent());
    }

    @Inject(method = "getDisabled", at = @At("HEAD"), cancellable = true)
    public void getDisabled(CallbackInfoReturnable<Color> cir) {
        cir.setReturnValue(VigilanceConfig.INSTANCE.getDisabled());
    }


    @Inject(method = "getBrightDividerState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getBrightDividerState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getBrightDivider()));
    }

    @Inject(method = "getDividerState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getDividerState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getDivider()));
    }

    @Inject(method = "getDarkDividerState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getDarkDividerState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getDarkDivider()));
    }

    @Inject(method = "getOutlineState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getOutlineState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getOutline()));
    }

    @Inject(method = "getScrollBarState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getScrollBarState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getScrollBar()));
    }

    @Inject(method = "getBrightHighlightState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getBrightHighlightState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getBrightHighlight()));
    }

    @Inject(method = "getHighlightState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getHighlightState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getHighlight()));
    }

    @Inject(method = "getDarkHighlightState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getDarkHighlightState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getDarkHighlight()));
    }

    @Inject(method = "getLightBackgroundState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getLightBackgroundState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getLightBackground()));
    }

    @Inject(method = "getBackgroundState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getBackgroundState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getBackground()));
    }

    @Inject(method = "getDarkBackgroundState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getDarkBackgroundState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getDarkBackground()));
    }

    @Inject(method = "getSearchBarBackgroundState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getSearchBarBackgroundState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getSearchBarBackground()));
    }

    @Inject(method = "getBrightTextState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getBrightTextState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getBrightText()));
    }

    @Inject(method = "getMidTextState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getMidTextState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getMidText()));
    }

    @Inject(method = "getDarkTextState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getDarkTextState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getDarkText()));
    }

    @Inject(method = "getModalBackgroundState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getModalBackgroundState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getModalBackground()));
    }

    @Inject(method = "getWarningState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getWarningState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getWarning()));
    }

    @Inject(method = "getAccentState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getAccentState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getAccent()));
    }

    @Inject(method = "getSuccessState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getSuccessState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getSuccess()));
    }

    @Inject(method = "getTransparentState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getTransparentState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getTransparent()));
    }

    @Inject(method = "getDisabledState$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getDisabledState$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(VigilanceConfig.INSTANCE.getDisabled()));
    }


    @Inject(method = "getBgNoAlpha$Vigilance1_8_9", at = @At("HEAD"), cancellable = true)
    public void getBgNoAlpha$Vigilance1_8_9(CallbackInfoReturnable<BasicState<Color>> cir) {
        cir.setReturnValue(new BasicState<>(new Color(VigilanceConfig.INSTANCE.getBackground().getRed(), VigilanceConfig.INSTANCE.getBackground().getGreen(), VigilanceConfig.INSTANCE.getBackground().getBlue(), 0)));
    }
}

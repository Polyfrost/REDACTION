package net.wyvest.redaction.mixin;

import gg.essential.elementa.state.BasicState;
import net.wyvest.redaction.config.VigilanceConfig;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Pseudo;

import java.awt.*;

@Pseudo
@Mixin(targets = "gg.essential.vigilance.gui.VigilancePalette", remap = false)
public class VigilancePaletteMixin {

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getBrightDivider() {
        return VigilanceConfig.INSTANCE.getBrightDivider();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getDivider() {
        return VigilanceConfig.INSTANCE.getDivider();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getDarkDivider() {
        return VigilanceConfig.INSTANCE.getDarkDivider();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getOutline() {
        return VigilanceConfig.INSTANCE.getOutline();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getScrollBar() {
        return VigilanceConfig.INSTANCE.getScrollBar();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getBrightHighlight() {
        return VigilanceConfig.INSTANCE.getBrightHighlight();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getHighlight() {
        return VigilanceConfig.INSTANCE.getHighlight();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getDarkHighlight() {
        return VigilanceConfig.INSTANCE.getDarkHighlight();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getLightBackground() {
        return VigilanceConfig.INSTANCE.getLightBackground();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getBackground() {
        return VigilanceConfig.INSTANCE.getBackground();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getDarkBackground() {
        return VigilanceConfig.INSTANCE.getDarkBackground();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getSearchBarBackground() {
        return VigilanceConfig.INSTANCE.getSearchBarBackground();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getBrightText() {
        return VigilanceConfig.INSTANCE.getBrightText();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getMidText() {
        return VigilanceConfig.INSTANCE.getMidText();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getDarkText() {
        return VigilanceConfig.INSTANCE.getDarkText();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getModalBackground() {
        return VigilanceConfig.INSTANCE.getModalBackground();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getWarning() {
        return VigilanceConfig.INSTANCE.getWarning();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getAccent() {
        return VigilanceConfig.INSTANCE.getAccent();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getSuccess() {
        return VigilanceConfig.INSTANCE.getSuccess();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getTransparent() {
        return VigilanceConfig.INSTANCE.getTransparent();
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final Color getDisabled() {
        return VigilanceConfig.INSTANCE.getDisabled();
    }

    /**
     * @author Wyvest
     */
    @NotNull
    @Overwrite
    public final BasicState<Color> getBrightDividerState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getBrightDivider());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getDividerState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getDivider());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getDarkDividerState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getDarkDivider());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getOutlineState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getOutline());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getScrollBarState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getScrollBar());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getBrightHighlightState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getBrightHighlight());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getHighlightState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getHighlight());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getDarkHighlightState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getDarkHighlight());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getLightBackgroundState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getLightBackground());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getBackgroundState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getBackground());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getDarkBackgroundState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getDarkBackground());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getSearchBarBackgroundState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getSearchBarBackground());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getBrightTextState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getBrightText());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getMidTextState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getMidText());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getDarkTextState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getDarkText());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getModalBackgroundState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getModalBackground());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getWarningState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getWarning());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getAccentState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getAccent());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getSuccessState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getSuccess());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getTransparentState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getTransparent());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getDisabledState$Vigilance1_8_9() {
        return new BasicState<>(VigilanceConfig.INSTANCE.getDisabled());
    }

    /**
     * @author Wyvest
     */
    @Overwrite
    @NotNull
    public final BasicState<Color> getBgNoAlpha$Vigilance1_8_9() {
        return new BasicState<>(new Color(VigilanceConfig.INSTANCE.getBackground().getRed(), VigilanceConfig.INSTANCE.getBackground().getGreen(), VigilanceConfig.INSTANCE.getBackground().getBlue(), 0));
    }
}

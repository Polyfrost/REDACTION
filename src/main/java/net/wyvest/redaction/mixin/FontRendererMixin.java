package net.wyvest.redaction.mixin;

import net.minecraft.client.gui.FontRenderer;
import net.wyvest.redaction.config.RedactionConfig;
import net.wyvest.redaction.features.NameHighlight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FontRenderer.class)
public class FontRendererMixin {
    @ModifyVariable(method = "renderString", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String onStringRendered_modifyText(String original) {
        return RedactionConfig.INSTANCE.getHighlightName() && original != null ? NameHighlight.highlightName(original) : original;
    }
}

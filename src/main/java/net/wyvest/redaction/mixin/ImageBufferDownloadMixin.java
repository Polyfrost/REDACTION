package net.wyvest.redaction.mixin;

import net.minecraft.client.renderer.ImageBufferDownload;
import net.wyvest.redaction.config.RedactionConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

@Mixin(ImageBufferDownload.class)
public class ImageBufferDownloadMixin {
    @Shadow private int[] imageData;

    @Shadow private int imageWidth;

    @Shadow private int imageHeight;

    @Shadow private void setAreaOpaque(int x, int y, int width, int height) {
        throw new AssertionError("REDACTION shadow mixin methods have failed! Please contact support in https://inv.wtf/w-overflow.");
    }

    @Shadow private void setAreaTransparent(int x, int y, int width, int height) {
        throw new AssertionError("REDACTION shadow mixin methods have failed! Please contact support in https://inv.wtf/w-overflow.");
    }

    @Inject(method = "parseUserSkin", at = @At("HEAD"), cancellable = true)
    private void fixSkinRendering(BufferedImage image, CallbackInfoReturnable<BufferedImage> cir) {
        if (image != null && RedactionConfig.INSTANCE.getFixSkinRendering()) {
            this.imageWidth = 64;
            this.imageHeight = 64;
            BufferedImage bufferedimage = new BufferedImage(this.imageWidth, this.imageHeight, 2);
            Graphics graphics = bufferedimage.getGraphics();
            graphics.drawImage(image, 0, 0, null);

            if (image.getHeight() == 32)
            {
                graphics.drawImage(bufferedimage, 24, 48, 20, 52, 4, 16, 8, 20, null);
                graphics.drawImage(bufferedimage, 28, 48, 24, 52, 8, 16, 12, 20, null);
                graphics.drawImage(bufferedimage, 20, 52, 16, 64, 8, 20, 12, 32, null);
                graphics.drawImage(bufferedimage, 24, 52, 20, 64, 4, 20, 8, 32, null);
                graphics.drawImage(bufferedimage, 28, 52, 24, 64, 0, 20, 4, 32, null);
                graphics.drawImage(bufferedimage, 32, 52, 28, 64, 12, 20, 16, 32, null);
                graphics.drawImage(bufferedimage, 40, 48, 36, 52, 44, 16, 48, 20, null);
                graphics.drawImage(bufferedimage, 44, 48, 40, 52, 48, 16, 52, 20, null);
                graphics.drawImage(bufferedimage, 36, 52, 32, 64, 48, 20, 52, 32, null);
                graphics.drawImage(bufferedimage, 40, 52, 36, 64, 44, 20, 48, 32, null);
                graphics.drawImage(bufferedimage, 44, 52, 40, 64, 40, 20, 44, 32, null);
                graphics.drawImage(bufferedimage, 48, 52, 44, 64, 52, 20, 56, 32, null);
                graphics.dispose();
                this.imageData = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
                this.setAreaOpaque(0, 0, 32, 16);
                this.setAreaTransparent(32, 0, 64, 32);
                this.setAreaOpaque(0, 16, 64, 32);
                this.setAreaTransparent(0, 32, 16, 48);
                this.setAreaTransparent(16, 32, 40, 48);
                this.setAreaTransparent(40, 32, 56, 48);
                this.setAreaTransparent(0, 48, 16, 64);
                this.setAreaOpaque(16, 48, 48, 64);
                this.setAreaTransparent(48, 48, 64, 64);
                cir.setReturnValue(bufferedimage);
                return;
            }
            cir.setReturnValue(image);
        }
    }
}

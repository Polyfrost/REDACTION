package net.wyvest.redaction.plugin;

import cc.woverflow.onecore.tweaker.OneCoreTweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class RedactionTweaker extends OneCoreTweaker {
    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.redaction.json");
        super.injectIntoClassLoader(classLoader);
    }
}

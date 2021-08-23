package net.wyvest.redaction.tweaker;

import gg.essential.loader.stage0.EssentialSetupTweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import xyz.matthewtgm.requisite.launchwrapper.RequisiteLaunchwrapper;

public class RequisiteEssentialTweaker extends EssentialSetupTweaker {

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        super.injectIntoClassLoader(classLoader);
        System.out.println("Essential has been loaded.");
        RequisiteLaunchwrapper.inject(classLoader);
        System.out.println("Requisite has been loaded.");
    }

}
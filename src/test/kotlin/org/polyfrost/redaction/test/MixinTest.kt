package org.polyfrost.redaction.test

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.MixinEnvironment.Option
import org.spongepowered.asm.mixin.transformer.IMixinTransformer

//? if >1.8.9 {
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
//?}

/**
 * Audits mixins for validity without launching a full Minecraft client
 * Inspired by [Skyblocker](https://github.com/SkyblockerMod/Skyblocker)
 */
class MixinTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun setupEnvironment() {
            // OSL's registry sync isn't initialized in unit tests, so bootstrapping 1.8.9 throws
            //? if >1.8.9 {
            SharedConstants.tryDetectVersion()
            Bootstrap.bootStrap()
            //?}
        }
    }

    @Test
    fun `mixins load successfully`() {
        val environment = MixinEnvironment.getCurrentEnvironment()
        Assertions.assertInstanceOf(
            IMixinTransformer::class.java,
            environment.activeTransformer,
        )
        // Refmap remapping retries target selection without the descriptor so turn it off to match production strictness
        environment.setOption(Option.REFMAP_REMAP, false)
        environment.audit()
    }
}

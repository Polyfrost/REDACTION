package net.wyvest.redaction.features.particles

import net.minecraft.block.material.Material
import net.minecraft.client.particle.*
import net.minecraft.util.EnumParticleTypes
import net.minecraft.init.Items
import net.wyvest.redaction.hook.EntityBreakingFXHook
import net.wyvest.redaction.hook.EntityCrit2FXHook
import net.wyvest.redaction.mixin.EntityDropParticleFXAccessor
import net.wyvest.redaction.mixin.EntityFXAccessor
import java.awt.Color

@Suppress("USELESS_CAST")
data class Particle(var enabled: Boolean = true, var tint: Int = Color.WHITE.rgb, var scale: Int = 1, var multiplier: Int = 1, @Transient val displayName: String, @Transient val particle: EnumParticleTypes, @Transient val instance: Class<out EntityFX>, @Transient val condition: (particle: EntityFX) -> Boolean) {
    companion object {
        val EXPLOSION_NORMAL = Particle(displayName = "Normal Explosion", particle = EnumParticleTypes.EXPLOSION_NORMAL, instance = EntityExplodeFX::class.java, condition = { it is EntityExplodeFX })
        val EXPLOSION_LARGE = Particle(displayName = "Large Explosion", particle = EnumParticleTypes.EXPLOSION_LARGE, instance = EntityLargeExplodeFX::class.java, condition = { it is EntityLargeExplodeFX })
        val EXPLOSION_HUGE = Particle(displayName = "Huge Explosion", particle = EnumParticleTypes.EXPLOSION_HUGE, instance = EntityHugeExplodeFX::class.java, condition = { it is EntityHugeExplodeFX })
        val FIREWORKS_SPARK = Particle(displayName = "Firework Spark", particle = EnumParticleTypes.FIREWORKS_SPARK, instance = EntityFirework.SparkFX::class.java, condition = { it is EntityFirework.SparkFX })
        val WATER_BUBBLE = Particle(displayName = "Water Bubble", particle = EnumParticleTypes.WATER_BUBBLE, instance = EntityBubbleFX::class.java, condition = { it is EntityBubbleFX })
        val WATER_SPLASH = Particle(displayName = "Water Splash", particle = EnumParticleTypes.WATER_SPLASH, instance = EntitySplashFX::class.java, condition = { it is EntitySplashFX })
        val WATER_WAKE = Particle(displayName = "Fish Water Splash", particle = EnumParticleTypes.WATER_WAKE, instance = EntityFishWakeFX::class.java, condition = { it is EntityFishWakeFX })
        val SUSPENDED = Particle(displayName = "Water Suspend Splash", particle = EnumParticleTypes.SUSPENDED, instance = EntitySuspendFX::class.java, condition = { it is EntitySuspendFX })
        val SUSPENDED_DEPTH = Particle(displayName = "Other Suspend Splash / Town Aura", particle = EnumParticleTypes.SUSPENDED_DEPTH, instance = EntityAuraFX::class.java, condition = { it is EntityAuraFX })
        val CRIT = Particle(displayName = "Critical", particle = EnumParticleTypes.CRIT, instance = EntityCrit2FX::class.java, condition = { it is EntityCrit2FX && !(it as EntityCrit2FX as EntityCrit2FXHook).isMagic })
        val CRIT_MAGIC = Particle(displayName = "Enchantment / Magic Critical", particle = EnumParticleTypes.CRIT_MAGIC, instance = EntityCrit2FX::class.java, condition = { it is EntityCrit2FX && (it as EntityCrit2FX as EntityCrit2FXHook).isMagic })
        val SMOKE_NORMAL = Particle(displayName = "Normal Smoke", particle = EnumParticleTypes.SMOKE_NORMAL, instance = EntitySmokeFX::class.java, condition = { it is EntitySmokeFX })
        val SMOKE_LARGE = Particle(displayName = "Large Smoke", particle = EnumParticleTypes.SMOKE_LARGE, instance = EntityCritFX::class.java, condition = { it is EntityCritFX })
        val SPELL = Particle(displayName = "Potion", particle = EnumParticleTypes.SPELL, instance = EntitySpellParticleFX::class.java, condition = { it is EntitySpellParticleFX })
        val DRIP_WATER = Particle(displayName = "Water Drip", particle = EnumParticleTypes.DRIP_WATER, instance = EntityDropParticleFX::class.java, condition = { it is EntityDropParticleFX && (it as EntityDropParticleFXAccessor).material == Material.water })
        val DRIP_LAVA = Particle(displayName = "Lava Drip", particle = EnumParticleTypes.DRIP_LAVA, instance = EntityDropParticleFX::class.java, condition = { it is EntityDropParticleFX && (it as EntityDropParticleFXAccessor).material == Material.lava })
        val VILLAGER_ANGRY = Particle(displayName = "Angry Villager", particle = EnumParticleTypes.VILLAGER_ANGRY, instance = EntityHeartFX::class.java, condition = { it is EntityHeartFX && (it as EntityFXAccessor).particleTextureX == (81 % 16) })
        val VILLAGER_HAPPY = Particle(displayName = "Happy Villager", particle = EnumParticleTypes.VILLAGER_HAPPY, instance = EntityAuraFX::class.java, condition = { it is EntityAuraFX && (it as EntityFXAccessor).particleTextureX == (82 % 16) })
        val NOTE = Particle(displayName = "Note Block", particle = EnumParticleTypes.NOTE, instance = EntityNoteFX::class.java, condition = { it is EntityNoteFX })
        val PORTAL = Particle(displayName = "Portal", particle = EnumParticleTypes.PORTAL, instance = EntityPortalFX::class.java, condition = { it is EntityPortalFX })
        val ENCHANTMENT_TABLE = Particle(displayName = "Enchantment Table", particle = EnumParticleTypes.ENCHANTMENT_TABLE, instance = EntityEnchantmentTableParticleFX::class.java, condition = { it is EntityEnchantmentTableParticleFX })
        val FLAME = Particle(displayName = "Fire", particle = EnumParticleTypes.FLAME, instance = EntityFlameFX::class.java, condition = { it is EntityFlameFX })
        val LAVA = Particle(displayName = "Lava", particle = EnumParticleTypes.LAVA, instance = EntityLavaFX::class.java, condition = { it is EntityLavaFX })
        val FOOTSTEP = Particle(displayName = "Footstep", particle = EnumParticleTypes.FOOTSTEP, instance = EntityFootStepFX::class.java, condition = { it is EntityFootStepFX })
        val CLOUD = Particle(displayName = "Cloud", particle = EnumParticleTypes.CLOUD, instance = EntityCloudFX::class.java, condition = { it is EntityCloudFX })
        val REDSTONE = Particle(displayName = "Redstone", particle = EnumParticleTypes.REDSTONE, instance = EntityReddustFX::class.java, condition = { it is EntityReddustFX })
        val SNOWBALL = Particle(displayName = "Snowball", particle = EnumParticleTypes.SNOWBALL, instance = EntityBreakingFX::class.java, condition = { it is EntityBreakingFX && (it as EntityBreakingFXHook).item == Items.snowball })
        val SNOW_SHOVEL = Particle(displayName = "Snow Shovel", particle = EnumParticleTypes.SNOW_SHOVEL, instance = EntitySnowShovelFX::class.java, condition = { it is EntitySnowShovelFX })
        val SLIME = Particle(displayName = "Slime", particle = EnumParticleTypes.SLIME, instance = EntityBreakingFX::class.java, condition = { it is EntityBreakingFX && (it as EntityBreakingFXHook).item == Items.slime_ball })
        val HEART = Particle(displayName = "Heart / Breeding", particle = EnumParticleTypes.HEART, instance = EntityHeartFX::class.java, condition = { it is EntityHeartFX && (it as EntityFXAccessor).particleTextureX != (81 % 16) })
        val BARRIER = Particle(displayName = "Barrier", particle = EnumParticleTypes.BARRIER, instance = Barrier::class.java, condition = { it is Barrier })
        val ITEM_CRACK = Particle(displayName = "Item Crack", particle = EnumParticleTypes.ITEM_CRACK, instance = EntityBreakingFX::class.java, condition = { it is EntityBreakingFX && (it as EntityBreakingFXHook).item != Items.snowball && (it as EntityBreakingFXHook).item != Items.slime_ball })
        val BLOCK_CRACK = Particle(displayName = "Block Breaking", particle = EnumParticleTypes.BLOCK_CRACK, instance = EntityDiggingFX::class.java, condition = { it is EntityDiggingFX })
        val BLOCK_DUST = Particle(displayName = "Block Broken Particles", particle = EnumParticleTypes.BLOCK_DUST, instance = EntityBlockDustFX::class.java, condition = { it is EntityBlockDustFX })
        val WATER_DROP = Particle(displayName = "Water Drop", particle = EnumParticleTypes.WATER_DROP, instance = EntityRainFX::class.java, condition = { it is EntityRainFX })
        val MOB_APPEARANCE = Particle(displayName = "Elder Guardian Overlay", particle = EnumParticleTypes.MOB_APPEARANCE, instance = MobAppearance::class.java, condition = { it is MobAppearance })

        @JvmField
        val values = arrayOf(EXPLOSION_NORMAL, EXPLOSION_LARGE, EXPLOSION_HUGE, FIREWORKS_SPARK, WATER_BUBBLE, WATER_SPLASH, WATER_WAKE, SUSPENDED, SUSPENDED_DEPTH, CRIT, CRIT_MAGIC, SMOKE_NORMAL, SMOKE_LARGE, SPELL, DRIP_WATER, DRIP_LAVA, VILLAGER_ANGRY, VILLAGER_HAPPY, NOTE, PORTAL, ENCHANTMENT_TABLE, FLAME, LAVA, FOOTSTEP, CLOUD, REDSTONE, SNOWBALL, SNOW_SHOVEL, SLIME, HEART, BARRIER, ITEM_CRACK, BLOCK_CRACK, BLOCK_DUST, WATER_DROP, MOB_APPEARANCE)
    }
}
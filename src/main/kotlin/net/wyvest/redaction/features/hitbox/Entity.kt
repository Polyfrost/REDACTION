package net.wyvest.redaction.features.hitbox

import com.google.gson.annotations.SerializedName
import java.awt.Color
import java.util.*

data class Entity(@Transient val name: String, @SerializedName("hitbox_enabled") var hitboxEnabled: Boolean = true, @SerializedName("eyeline_enabled") var eyeLineEnabled: Boolean = true, @SerializedName("line_enabled") var lineEnabled: Boolean = true, var color: Int = Color.WHITE.rgb, @SerializedName("crosshair_color") var crosshairColor: Int = Color.WHITE.rgb, @SerializedName("eye_color") var eyeColor: Int = Color.RED.rgb, @SerializedName("line_color") var lineColor: Int = Color.BLUE.rgb, @Transient val condition: (net.minecraft.entity.Entity) -> Boolean, @Transient private val priority: Int) {

    override fun toString(): String {
        return this.name.lowercase().replaceFirstChar { char: Char ->
            if (char.isLowerCase()) char.titlecase(
                Locale.ENGLISH
            ) else char.toString()
        }
    }

    companion object {
        val blank = Entity("", condition = {true}, priority = 0)
        private val armorStand = Entity("Armor Stand", condition = { it is net.minecraft.entity.item.EntityArmorStand }, priority = 0)
        private val fireball = Entity("Fireball", condition = { it is net.minecraft.entity.projectile.EntityFireball }, priority = 0)
        private val firework = Entity("Firework", condition = {it is net.minecraft.entity.item.EntityFireworkRocket}, priority = -700)
        private val item = Entity("Item", condition = { it is net.minecraft.entity.item.EntityItem }, priority = -900)
        private val itemFrame = Entity("Item Frame", condition = { it is net.minecraft.entity.item.EntityItemFrame }, priority = -800)
        private val living = Entity("Living", condition = {it is net.minecraft.entity.EntityLiving}, priority = Int.MIN_VALUE + 7)
        private val monster = Entity("Monster", condition = { it is net.minecraft.entity.monster.IMob }, priority = Int.MIN_VALUE + 6)
        private val minecart = Entity("Minecart", condition = { it is net.minecraft.entity.item.EntityMinecart }, priority = -1000)
        private val player = Entity("Player", condition = { it is net.minecraft.entity.player.EntityPlayer }, priority = Int.MIN_VALUE + 1)
        private val self = Entity("Self", condition = { it is net.minecraft.client.entity.EntityPlayerSP && (it as net.minecraft.entity.player.EntityPlayer).gameProfile.id == net.minecraft.client.Minecraft.getMinecraft().thePlayer.gameProfile.id }, priority = Int.MIN_VALUE)
        private val projectile = Entity("Projectile", condition = {it is net.minecraft.entity.IProjectile}, priority = 1)
        private val witherSkull = Entity("Wither Skull", condition = { it is net.minecraft.entity.projectile.EntityWitherSkull }, priority = Int.MIN_VALUE + 200)
        private val undefined = Entity("Undefined", condition = {true}, priority = Int.MAX_VALUE)
        private val xp = Entity("XP", condition = { it is net.minecraft.entity.item.EntityXPOrb }, priority = -600)

        @Transient @JvmStatic val map = linkedMapOf(
            0 to armorStand,
            1 to fireball,
            2 to firework,
            3 to item,
            4 to itemFrame,
            5 to living,
            6 to monster,
            7 to minecart,
            8 to minecart,
            9 to player,
            10 to self,
            11 to projectile,
            12 to witherSkull,
            13 to undefined,
            14 to xp
        )

        @Transient @JvmStatic val sortedList = map.values.sortedWith(Comparator.comparingInt(Entity::priority))
    }
}
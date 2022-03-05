package net.wyvest.redaction.plugin

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class RedactionMixinPlugin : IMixinConfigPlugin {
    private var returned = false

    override fun onLoad(mixinPackage: String?) {

    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {

    }

    override fun getMixins(): MutableList<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {

    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {
        if (!returned) {
            if (targetClass != null && targetClassName == "net.minecraft.client.renderer.entity.RenderManager") {
                for (method in targetClass.methods) {
                    if (method.name == "renderDebugBoundingBox" || method.name == "func_85094_b") {
                        for (insn in method.instructions) {
                            if (insn.opcode == Opcodes.INVOKESPECIAL && (insn as MethodInsnNode).name.contains("cancelIfEmulatedPlayer")) {
                                val fieldinsn = FieldInsnNode(Opcodes.GETSTATIC, "net/wyvest/redaction/gui/HitboxPreviewGUI", "Companion", "Lnet/wyvest/redaction/gui/HitboxPreviewGUI\$Companion;")
                                val methodinsn = MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/wyvest/redaction/gui/HitboxPreviewGUI\$Companion", "getBypassHitbox", "()Z", false)
                                val jumpinsn = JumpInsnNode(Opcodes.IFNE, run {
                                    method.instructions.iterator(method.instructions.indexOf(insn)).forEach {
                                        if (it is LabelNode) {
                                            return@run it
                                        }
                                    }
                                    returned = true // this is set to true so mods like crashpatch can recover from these crashes
                                    throw RuntimeException("REDACTION ASM Failed, please go to https://woverflow.cc/discord for support!")
                                })
                                method.instructions.insert(insn.next.next.next, fieldinsn)
                                method.instructions.insert(fieldinsn, methodinsn)
                                method.instructions.insert(methodinsn, jumpinsn)
                                returned = true
                            }
                        }
                    }
                }
            }
        }
    }
}
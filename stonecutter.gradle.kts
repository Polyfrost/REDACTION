plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.3" /* [SC] DO NOT EDIT */

stonecutter {
    parameters {
        replacements {
            string(current.parsed < "26.3") {
                replace("com.mojang.renderpearl.api.pipeline.RenderPipeline", "com.mojang.blaze3d.pipeline.RenderPipeline")
                replace("com.mojang.renderpearl.api.pipeline.PrimitiveTopology", "com.mojang.blaze3d.PrimitiveTopology")
            }
            string(current.parsed < "26.1") {
                replace("GuiGraphicsExtractor", "GuiGraphics")
            }
            // `ResourceLocation` was renamed to `Identifier` in 1.21.11,
            // and `RenderType` moved to the `renderer.rendertype` package in the same version.
            string(current.parsed < "1.21.11") {
                replace("Identifier", "ResourceLocation")
                replace("renderer.rendertype.RenderType", "renderer.RenderType")
            }
        }
    }

    tasks {
        order("publishModrinth")
    }
}

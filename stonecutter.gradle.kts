plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.2" /* [SC] DO NOT EDIT */

stonecutter {
    parameters {
        replacements {
            string(current.parsed < "26.1") {
                replace("GuiGraphicsExtractor", "GuiGraphics")
            }
            // `ResourceLocation` was renamed to `Identifier` in 1.21.11,
            // and `RenderType` moved to the `renderer.rendertype` package in the same version.
            string(eval(current.version, "< 1.21.11", "> 1.8.9")) {
                replace("Identifier", "ResourceLocation")
                replace("renderer.rendertype.RenderType", "renderer.RenderType")
            }
        }
    }

    tasks {
        order("publishModrinth")
    }
}

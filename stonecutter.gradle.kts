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

            string(current.parsed < "1.21.11") {
                replace("Identifier", "ResourceLocation")
            }
        }
    }

    tasks {
        order("publishModrinth")
    }
}

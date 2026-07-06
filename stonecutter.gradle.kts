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
        }
    }

    tasks {
        order("publishModrinth")
    }
}

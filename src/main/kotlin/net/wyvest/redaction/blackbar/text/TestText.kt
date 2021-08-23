package net.wyvest.redaction.blackbar.text

class TestText : Text {

    override fun getText(): String {
        return "Test"
    }
    override fun getPrefix(): String {
        return "What"
    }

}
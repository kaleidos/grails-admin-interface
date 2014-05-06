package net.kaleidos.plugins.admin.widget

class TextAreaWidget extends Widget {

    TextAreaWidget(String value, Map attrs = [:]) {
        super(value, attrs)
    }

    TextAreaWidget(Map attrs) {
        super(null, attrs)
    }

    TextAreaWidget() {
        super(null, [:])
    }

    String render() {
        String html = "<textarea"
        attrs.each {key, value ->
            html += " ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\""
        }
        html += ">"
        if (value) {
            html += "${value.encodeAsHTML()}"
        }
        html += "</textarea>"
        return html
    }
}

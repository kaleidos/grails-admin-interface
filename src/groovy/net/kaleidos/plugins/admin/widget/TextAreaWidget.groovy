package net.kaleidos.plugins.admin.widget

class TextAreaWidget extends Widget {

    TextAreaWidget(String value, Map<String, String> attrs = [:]) {
        super(value, attrs)
    }

    TextAreaWidget(Map<String, String> attrs) {
        super(null, attrs)
    }

    TextAreaWidget() {
        super(null, [:])
    }

    String render() {
        String html = "<textarea"
        attrs.each {key, value ->
            html += " ${key}=\"${value}\""
        }
        html += ">"
        if (value) {
            html += "${value}"
        }
        html += "</textarea>"
        return html
    }
}

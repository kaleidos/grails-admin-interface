package net.kaleidos.plugins.admin.widget

abstract class InputWidget extends Widget{
    String inputType


    InputWidget(Object value, Map attrs) {
        super(value, attrs)
    }

    String render() {
        String html = "<input type=\"${inputType}\""
        if (value) {
            html += " value=\"${value}\""
        }
        attrs.each {key, value ->
            html += " ${key}=\"${value}\""
        }
        html +=" />"
        return html
    }
}

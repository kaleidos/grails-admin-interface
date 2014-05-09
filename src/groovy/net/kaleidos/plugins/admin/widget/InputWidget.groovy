package net.kaleidos.plugins.admin.widget

abstract class InputWidget extends Widget{
    String inputType


    InputWidget(Object value, Map attrs) {
        super(value, attrs)
    }

    @Override
    String render() {
        String html = "<input type=\"${inputType.encodeAsHTML()}\""
        if (value) {
            html += " value=\"${value.encodeAsHTML()}\""
        }
        attrs.each {key, value ->
            html += " ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\""
        }
        html +=" />"
        return html
    }
}

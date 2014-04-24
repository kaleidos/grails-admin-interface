package net.kaleidos.plugins.admin.widget

abstract class InputWidget extends Widget{
    String inputType
    def value

    InputWidget(Object value, Map<String, String> attrs) {
        super(attrs)
        this.value = value
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

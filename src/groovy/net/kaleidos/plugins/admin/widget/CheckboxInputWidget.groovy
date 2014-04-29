package net.kaleidos.plugins.admin.widget

class CheckboxInputWidget extends InputWidget{
    String text = null

    CheckboxInputWidget(String value, String text, Map<String, String> attrs) {
        super(value, attrs)
        inputType = "checkbox"
        this.text = text
    }

    CheckboxInputWidget() {
        super(null, [:])
        inputType = "checkbox"
    }

    String render() {
        String html = "<input type=\"${inputType}\""
        if (value) {
            html += " value=\"${value}\""
        }
        attrs.each {key, value ->
            html += " ${key}=\"${value}\""
        }
        html += ">"
        html += text?:""
        html += "</input>"
        return html
    }
}

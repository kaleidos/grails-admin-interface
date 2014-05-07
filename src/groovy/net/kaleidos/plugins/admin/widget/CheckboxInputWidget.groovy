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

    @Override
    String render() {
        String html = "<input type=\"${inputType.encodeAsHTML()}\""
        if (value) {
            html += " value=\"${value.encodeAsHTML()}\""
        }
        attrs.each {key, value ->
            html += " ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\""
        }
        html += ">"
        html += text?text.encodeAsHTML():""
        html += "</input>"
        return html
    }
}

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
		StringBuilder html = new StringBuilder()
        html.append("<input type=\"${inputType.encodeAsHTML()}\"")
        if (value) {
            html.append(" value=\"${value.encodeAsHTML()}\"")
        }
        attrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(">")
        html.append(text?text.encodeAsHTML():"")
        html.append("</input>")
        return html
    }
}

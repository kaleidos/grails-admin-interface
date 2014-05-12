package net.kaleidos.plugins.admin.widget

class CheckboxInputWidget extends InputWidget{
    String text = null

    CheckboxInputWidget() {
        inputType = "checkbox"
    }

    @Override
    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<input type=\"${inputType.encodeAsHTML()}\"")
        if (value) {
            html.append(" value=\"${value.encodeAsHTML()}\"")
        }
        htmlAttrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(">")
        html.append(text?text.encodeAsHTML():"")
        html.append("</input>")
        return html
    }
}

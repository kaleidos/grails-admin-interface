package net.kaleidos.plugins.admin.widget

class CheckboxInputWidget extends InputWidget{

    CheckboxInputWidget() {
        inputType = "checkbox"
    }

    @Override
    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<input type=\"${inputType.encodeAsHTML()}\"")
        if (value) {
            html.append(" checked=\"checked\"")
        }
        htmlAttrs.each {key, value ->
            if (key != "required") {
                html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
            }
        }
        html.append(">")
        html.append(internalAttrs["text"]?internalAttrs["text"].encodeAsHTML():"")
        html.append("</input>")
        return html
    }

    public void updateValue() {
        updateValue(value?true:false)
    }


}

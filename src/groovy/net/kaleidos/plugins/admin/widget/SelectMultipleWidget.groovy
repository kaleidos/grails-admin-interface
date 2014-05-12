package net.kaleidos.plugins.admin.widget

class SelectMultipleWidget extends Widget {

    SelectMultipleWidget(Object value, Map attrs=[:]) {
        super(value, attrs)
    }

    SelectMultipleWidget() {
        super(null, [:])
    }

    @Override
   
    String render() {
		StringBuilder html = new StringBuilder()
        html.append("<select multiple")
        attrs.each {key, value ->
            if (key != "options") {
                html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
            }
        }
        html.append(">")

        if (!attrs['required']) {
            html.append("<option value=\"\">--</option>")
        }

        // draw options values
        if (attrs.options) {
            attrs.options.each {val, text ->
                html.append("<option value=\"${val.encodeAsHTML()}\"")
                if (val == value) {
                    html.append(" selected=\"selected\"")
                }
                html.append(">${text.encodeAsHTML()}</option>")
            }
        }

        html.append("</select>")
        return html
    }
}

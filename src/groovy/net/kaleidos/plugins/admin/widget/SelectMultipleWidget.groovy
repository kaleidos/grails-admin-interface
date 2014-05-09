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
        String html = "<select multiple"
        attrs.each {key, value ->
            if (key != "options") {
                html += " ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\""
            }
        }
        html += ">"

        if (!attrs['required']) {
            html += "<option value=\"\">--</option>"
        }

        // draw options values
        if (attrs.options) {
            attrs.options.each {val, text ->
                html += "<option value=\"${val.encodeAsHTML()}\""
                if (val == value) {
                    html += " selected=\"selected\""
                }
                html += ">${text.encodeAsHTML()}</option>"
            }
        }

        html += "</select>"
        return html
    }
}

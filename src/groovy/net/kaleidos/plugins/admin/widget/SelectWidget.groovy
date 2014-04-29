package net.kaleidos.plugins.admin.widget

class SelectWidget extends Widget {

    SelectWidget(Object value, Map attrs=[:]) {
        super(value, attrs)
    }

    SelectWidget() {
        super(null, [:])
    }

    String render() {
        String html = "<select"
        attrs.each {key, value ->
            if (key != "options") {
                html += " ${key}=\"${value}\""
            }
        }
        html += ">"

        if (!attrs['required']) {
            html += "<option value=\"\">--</option>"
        }

        // draw options values
        if (attrs.options) {
            attrs.options.each {val, text ->
                html += "<option value=\"${val}\""
                if (val == value) {
                    html += " selected=\"selected\""
                }
                html += ">${text}</option>"
            }
        }

        html += "</select>"
        return html
    }
}

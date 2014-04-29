package net.kaleidos.plugins.admin.widget

class SelectWidget extends Widget {
    Map options = [:]
    Boolean nullable = false

    SelectWidget(Object value, Map attrs) {
        super(value, attrs)
    }

    SelectWidget(Object value, Map<String, String> attrs, Map options) {
        super(value, attrs)
        this.options = options
    }

    SelectWidget() {
        super(null, [:])
    }

    String render() {
        String html = "<select"
        attrs.each {key, value ->
            html += " ${key}=\"${value}\""
        }
        html += ">"

        if (nullable) {
            html += "<option value=\"\">--</option>"
        }

        // draw options values
        if (options) {
            options.each {val, text ->
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

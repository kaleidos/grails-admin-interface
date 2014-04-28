package net.kaleidos.plugins.admin.widget

class SelectWidget extends Widget {

    SelectWidget(Map<String, Object> value, Map<String, String> attrs) {
        super(value, attrs)
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

        // draw options values
        if (value) {
            value.each {value, text ->
                html += "<option value=\"${value}\">${text}</option>"
            }
        }

        html += "</select>"
        return html
    }
}

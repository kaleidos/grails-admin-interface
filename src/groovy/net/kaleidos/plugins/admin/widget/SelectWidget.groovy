package net.kaleidos.plugins.admin.widget

class SelectWidget extends Widget {

    SelectWidget(Object value, Map attrs=[:]) {
        super(value, attrs)
    }

    SelectWidget() {
        super(null, [:])
    }

    @Override
    String render() {

        String html = "<select"
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
                println "---->${val as String} ${value as String} ${(val as String) == (value as String)}"
                html += "<option value=\"${val.encodeAsHTML()}\""
                if ((val as String) == (value as String)) {
                    html += " selected=\"selected\""
                }
                html += ">${text.encodeAsHTML()}</option>"
            }
        }

        html += "</select>"
        return html
    }
}

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
		StringBuilder html = new StringBuilder()

        html.append("<select")
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
                println "---->${val as String} ${value as String} ${(val as String) == (value as String)}"
                html.append("<option value=\"${val.encodeAsHTML()}\"")
                if ((val as String) == (value as String)) {
                    html.append(" selected=\"selected\"")
                }
                html.append(">${text.encodeAsHTML()}</option>")
            }
        }

        html.append("</select>")
        return html
    }
}

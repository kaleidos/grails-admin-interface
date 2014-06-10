package net.kaleidos.plugins.admin.widget

class SelectMultipleWidget extends Widget {

    @Override

    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<select multiple")
        htmlAttrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(">")

        // draw options values
        if (internalAttrs.options) {
            internalAttrs.options.each {val, text ->
                html.append("<option value=\"${val.encodeAsHTML()}\"")
                if (val in value) {
                    html.append(" selected=\"selected\"")
                }
                html.append(">${text.encodeAsHTML()}</option>")
            }
        }

        html.append("</select>")
        return html
    }

    List<String> getAssets() {
        def results =[
            'libs/select2/select2.css',
            'libs/select2/select2-bootstrap.css',
            'libs/select2/select2.js'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }
}

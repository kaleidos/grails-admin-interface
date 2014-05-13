package net.kaleidos.plugins.admin.widget

class TextAreaWidget extends Widget {

    @Override
    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<textarea")
        htmlAttrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(">")
        if (value) {
            html.append("${value.encodeAsHTML()}")
        }
        html.append("</textarea>")
        return html
    }
}

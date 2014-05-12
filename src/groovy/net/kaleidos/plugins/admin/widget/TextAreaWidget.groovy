package net.kaleidos.plugins.admin.widget

class TextAreaWidget extends Widget {

    TextAreaWidget(String value, Map attrs = [:]) {
        super(value, attrs)
    }

    TextAreaWidget(Map attrs) {
        super(null, attrs)
    }

    TextAreaWidget() {
        super(null, [:])
    }

    @Override
    String render() {
		StringBuilder html = new StringBuilder()
        html.append("<textarea")
        attrs.each {key, value ->
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

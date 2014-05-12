package net.kaleidos.plugins.admin.widget

abstract class InputWidget extends Widget{
    String inputType


    InputWidget(Object value, Map attrs) {
        super(value, attrs)
    }

    @Override
    String render() {
		StringBuilder html = new StringBuilder()
        html.append("<input type=\"${inputType.encodeAsHTML()}\"")
        if (value) {
            html.append(" value=\"${value.encodeAsHTML()}\"")
        }
        attrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(" />")
        return html
    }
}

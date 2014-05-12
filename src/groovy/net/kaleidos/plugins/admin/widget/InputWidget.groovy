package net.kaleidos.plugins.admin.widget

abstract class InputWidget extends Widget{
    String inputType

    @Override
    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<input type=\"${inputType.encodeAsHTML()}\"")
        if (value) {
            html.append(" value=\"${value.encodeAsHTML()}\"")
        }
        htmlAttrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(" />")
        return html
    }
}

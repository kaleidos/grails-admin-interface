package net.kaleidos.plugins.admin.widget

class LabelWidget extends Widget {

    @Override
    String render() {
        StringBuilder html = new StringBuilder()
        html.append("<label")
        htmlAttrs.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(">")
        if (value) {
            html.append("${value.encodeAsHTML()}")
        }
        html.append("</label>")
        return html
    }


}

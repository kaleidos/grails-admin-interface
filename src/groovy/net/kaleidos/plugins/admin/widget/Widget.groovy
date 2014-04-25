package net.kaleidos.plugins.admin.widget

abstract class Widget {
    Map attrs = [:]
    def value

    Widget(Object value, Map<String, String> attrs) {
        this.attrs = attrs
        this.value = value
    }

    abstract String render()
}

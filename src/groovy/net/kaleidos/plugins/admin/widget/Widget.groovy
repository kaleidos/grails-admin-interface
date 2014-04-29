package net.kaleidos.plugins.admin.widget

abstract class Widget {
    Map attrs = [:]
    def value

    Widget(Object value, Map attrs) {
        this.attrs = attrs
        this.value = value
    }

    abstract String render()
}

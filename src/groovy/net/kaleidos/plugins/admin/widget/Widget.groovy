package net.kaleidos.plugins.admin.widget

abstract class Widget {
    Map attrs = [:]

    Widget(Map<String, String> attrs) {
        this.attrs = attrs
    }

    abstract String render()
}

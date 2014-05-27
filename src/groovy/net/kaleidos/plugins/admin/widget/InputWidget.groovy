package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

abstract class InputWidget extends Widget{
    String inputType

    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def attrs = htmlAttrs.clone()
        attrs << ["type": inputType]
        attrs << ["value": value?:""]

        builder.input(attrs)

        return writer
    }
}

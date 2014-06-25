package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class LabelWidget extends Widget {

    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        builder.label(htmlAttrs, value)

        return writer
    }

    public void updateValue() {
        //Do nothing
    }


}

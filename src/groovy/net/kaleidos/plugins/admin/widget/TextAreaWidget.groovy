package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class TextAreaWidget extends Widget {

    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        builder.textarea htmlAttrs, {
            mkp.yield "${value?:''}"
        }

        return writer
    }
}

package net.kaleidos.plugins.admin.widget

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import groovy.xml.MarkupBuilder

@CompileStatic
abstract class InputWidget extends Widget {
    String inputType

    @Override
    @CompileStatic(TypeCheckingMode.SKIP)
    String render() {
        StringWriter writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)

        Map attrs = (Map)htmlAttrs.clone()
        attrs << ["type": inputType]
        attrs << ["value": (value!=null)?value:""]

        builder.input(attrs)

        return writer
    }
}

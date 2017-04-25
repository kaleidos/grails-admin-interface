package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder
import net.kaleidos.plugins.admin.DomainInspector

class EnumWidget extends Widget {
    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def inspector = new DomainInspector(internalAttrs.domainClass)
        def type = inspector.getPropertyClass(internalAttrs.propertyName)

        if (!type || !type.isEnum()) {
            return "<p>The type ${type} is not a enum</p>"
        }

        def values = type.values().collect { it.name() }

        builder.select htmlAttrs, {
            if (!htmlAttrs['required']) {
                option "value":"", { mkp.yield "---" }
            }
            type.values().each { enumVal->
                def optionAttrs = ["value":enumVal.name()]
                if (enumVal == value) {
                    optionAttrs << ["selected":"selected"]
                }
                option optionAttrs, {
                    mkp.yield "${enumVal.name()}"
                }
            }
        }

        return writer
    }

    @Override
    List<String> getAssets() {
        List results = [
            'libs/select2/select2.css',
            'libs/select2/select2-bootstrap.css',
            'libs/select2/select2.js'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }

    void updateValue(value) {
        DomainInspector inspector = new DomainInspector(domainClass)
        Class type = inspector.getPropertyClass(propertyName)
        domainInstance[propertyName] = (value) ? Enum.valueOf(type, value) : null
    }
}

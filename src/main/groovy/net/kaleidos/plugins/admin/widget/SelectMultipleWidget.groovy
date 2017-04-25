package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class SelectMultipleWidget extends Widget {

    @Override

    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        def attrs = htmlAttrs.clone()

        attrs << ['multiple': '']

        builder.select(attrs) {
            if (internalAttrs.options) {
                internalAttrs.options.each {val, text ->
                    def optionAttrs = ["value": val]
                    if (val in value) {
                        optionAttrs << ["selected": "selected"]
                    }
                    option(optionAttrs, text)
                }
            } else {
                mkp.yield("")
            }
        }
        return writer
    }

    List<String> getAssets() {
        def results = [
            'grails-admin/libs/select2/select2.css',
            'grails-admin/libs/select2/select2-bootstrap.css',
            'grails-admin/libs/select2/select2.js'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }
}

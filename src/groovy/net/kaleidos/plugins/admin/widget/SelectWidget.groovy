package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class SelectWidget extends Widget {

    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        builder.select(htmlAttrs) {
            if (!htmlAttrs['required']) {
                option(value:"", "--")
            }

            if (internalAttrs.options) {
                internalAttrs.options.each {val, text ->
                    def optionAttrs = ["value": val]
                    if ((val as String) == (value as String)) {
                        optionAttrs << ["selected": "selected"]
                    }
                    option(optionAttrs, text)
                }
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

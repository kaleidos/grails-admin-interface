package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class CheckboxInputWidget extends InputWidget{

    CheckboxInputWidget() {
        inputType = "checkbox"
    }

    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        def attrs = htmlAttrs.clone()

        attrs << ["type": inputType]
        if (value) {
            attrs << ["checked": "checked"]
        }

        attrs.remove("required")

        if(internalAttrs["text"]) {
            builder.input(attrs, internalAttrs["text"])
        } else {
            builder.input(attrs)
        }

        return writer
    }

    public void updateValue() {
        updateValue(value?true:false)
    }


}

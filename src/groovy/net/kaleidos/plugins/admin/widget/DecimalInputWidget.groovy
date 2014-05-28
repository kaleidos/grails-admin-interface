package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class DecimalInputWidget extends Widget{
    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def attrs = htmlAttrs.clone()

        attrs << ["data-parsley-type": "number"]
        attrs << ["value": value?:""]

        builder.input(attrs)

        return writer
    }

    @Override
    def getValueForJson() {
        return value
    }

    @Override
    public void updateValue() {
        if (value) {
            super.updateValue(parse("$value"))
        }
    }

    def parse(String str) {
        def number = null;
        try {
            number = Float.parseFloat(str)
        } catch(NumberFormatException floatException) {
            try {
                number = Double.parseDouble(str)
            } catch(NumberFormatException doubleException) {
                throw doubleException
            }
        }
        return number;
    }
}

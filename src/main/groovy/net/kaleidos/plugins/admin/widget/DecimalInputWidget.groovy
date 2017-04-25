package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class DecimalInputWidget extends Widget{
    @Override
    String render() {
        String writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)

        Map attrs = htmlAttrs.clone()

        attrs << ["data-parsley-type": "number"]
        attrs << ["value": (value!=null)?value:""]

        builder.input(attrs)

        return writer
    }

    @Override
    String getValueForJson() {
        return value
    }

    @Override
    public void updateValue() {
        if (value) {
            Object.updateValue(parse("$value"))
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

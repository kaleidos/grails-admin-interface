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
        super.updateValue(parse("$value"))
    }

    def parse(String str) {
        def number = null;
        try {
            number = Double.parseDouble(str);
        } catch(NumberFormatException e) {
            try {
                number = Float.parseFloat(str);
            } catch(NumberFormatException e1) {
                try {
                    number = Long.parseLong(str);
                } catch(NumberFormatException e2) {
                    try {
                        number = Integer.parseInt(str);
                    } catch(NumberFormatException e3) {
                        throw e3;
                    }
                }
            }
        }
        return number;
    }
}

package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class DateInputWidget extends InputWidget{
    static String DEFAULT_DATE_FORMAT = "MM/dd/yyyy"

    DateInputWidget() {
        inputType = "date"
    }

    def getValueForJson() {
        def format = _getFormat()
        return value?value.format(format):""
    }


    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def attrs = htmlAttrs.clone()
        attrs << ["type": inputType]
        def format = _getFormat()
        attrs << ["value": value?value.format(format):""]

        builder.input(attrs)

        return writer
    }


    List<String> getAssets() {
        return [ 'libs/bootstrap-datepicker/css/datepicker3.css', 'libs/bootstrap-datepicker/js/bootstrap-datepicker.js']
    }


    public void updateValue() {
        def format = _getFormat()
        updateValue(Date.parse(format, value))
    }

    String _getFormat(){
        return internalAttrs["dateFormat"]?:DEFAULT_DATE_FORMAT
    }
}

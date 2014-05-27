package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class DateInputWidget extends Widget{
    static String DEFAULT_DATE_FORMAT = "MM/dd/yyyy"

    def getValueForJson() {
        def format = _getFormat()
        return value?value.format(format):""
    }


    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def format = _getFormat()

        def attrs = htmlAttrs.clone()
        attrs << ["type": "text"]
        attrs << ["class": "date"]
        attrs << ["value": value?value.format(format):""]


        builder.input(attrs)

        return writer
    }


    List<String> getAssets() {
        return [
            'grails-admin/libs/bootstrap-datepicker/css/datepicker3.css',
            'grails-admin/libs/bootstrap-datepicker/js/bootstrap-datepicker.js',
            'grails-admin/js/datepicker.js']
    }


    public void updateValue() {
        def format = _getFormat()
        updateValue(Date.parse(format, value))
    }

    String _getFormat(){
        return internalAttrs["dateFormat"]?:DEFAULT_DATE_FORMAT
    }
}

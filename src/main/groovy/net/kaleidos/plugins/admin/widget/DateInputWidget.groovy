package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class DateInputWidget extends Widget {
    static String DEFAULT_DATE_FORMAT = "dd/MM/yyyy"

    def getValueForJson() {
        String format = _getFormat()
        Date date = (Date)value
        return date ? date.format(format) : ""
    }


    @Override
    String render() {
        String writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)

        String format = _getFormat()

        Map attrs = htmlAttrs.clone()
        attrs << ["type": "text"]
        attrs << ["data-date-format": format.toLowerCase()]
        attrs << ["class": "date form-control"]
        attrs << ["value": value?value.format(format):""]


        builder.input(attrs)

        return writer
    }


    List<String> getAssets() {
        List results = [
            'grails-admin/libs/bootstrap-datepicker/css/datepicker3.css',
            'grails-admin/libs/bootstrap-datepicker/js/bootstrap-datepicker.js',
            'grails-admin/js/datepicker.js'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }


    void updateValue() {
        if (value) {
            String format = _getFormat()
            updateValue(Date.parse(format, value))
        } else {
            value = null
        }
    }

    String _getFormat(){
        return internalAttrs["dateFormat"] ?: DEFAULT_DATE_FORMAT
    }
}

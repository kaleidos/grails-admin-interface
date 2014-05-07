package net.kaleidos.plugins.admin.widget

class DateInputWidget extends InputWidget{

    DateInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "date"
    }

    DateInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "date"
    }

    DateInputWidget() {
        super(null, [:])
        inputType = "date"
    }

    @Override
    String renderAsJson() {
        println ">>>>> Date widget"
        // if (value instanceof Date /* && attrs.format*/) {
            // return value.format(attrs.format)



            // def dateValue = value as Date
            // return (dateValue).format('dd/MM/yyyy')




        // } else {
            super.renderAsJson()
        // }
    }

}

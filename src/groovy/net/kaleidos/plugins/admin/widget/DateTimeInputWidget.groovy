package net.kaleidos.plugins.admin.widget

class DateTimeInputWidget extends InputWidget{

    DateTimeInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "datetime"
    }

    DateTimeInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "datetime"
    }

    DateTimeInputWidget() {
        super(null, [:])
        inputType = "datetime"
    }


}

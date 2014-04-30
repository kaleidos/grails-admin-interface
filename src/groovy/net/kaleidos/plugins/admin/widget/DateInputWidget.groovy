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


}

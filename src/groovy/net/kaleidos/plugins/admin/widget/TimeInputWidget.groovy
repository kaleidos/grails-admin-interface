package net.kaleidos.plugins.admin.widget

class TimeInputWidget extends InputWidget{

    TimeInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "time"
    }

    TimeInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "time"
    }

    TimeInputWidget() {
        super(null, [:])
        inputType = "time"
    }


}

package net.kaleidos.plugins.admin.widget

class EmailInputWidget extends InputWidget{

    EmailInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "email"
    }

    EmailInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "email"
    }

    EmailInputWidget() {
        super(null, [:])
        inputType = "email"
    }


}

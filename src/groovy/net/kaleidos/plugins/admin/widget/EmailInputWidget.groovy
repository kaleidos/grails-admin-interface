package net.kaleidos.plugins.admin.widget

class EmailInputWidget extends InputWidget{

    EmailInputWidget(String value, Map<String, String> attrs=[:]) {
        super(value, attrs)
        inputType = "email"
    }

    EmailInputWidget(Map<String, String> attrs) {
        super(null, attrs)
        inputType = "email"
    }

    EmailInputWidget() {
        super(null, [:])
        inputType = "email"
    }


}

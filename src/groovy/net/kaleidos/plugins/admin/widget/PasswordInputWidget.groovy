package net.kaleidos.plugins.admin.widget

class PasswordInputWidget extends InputWidget{

    PasswordInputWidget(String value, Map<String, String> attrs=[:]) {
        super(value, attrs)
        inputType = "password"
    }

    PasswordInputWidget(Map<String, String> attrs) {
        super(null, attrs)
        inputType = "password"
    }

    PasswordInputWidget() {
        super(null, [:])
        inputType = "password"
    }


}

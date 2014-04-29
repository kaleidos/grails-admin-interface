package net.kaleidos.plugins.admin.widget

class PasswordInputWidget extends InputWidget{

    PasswordInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "password"
    }

    PasswordInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "password"
    }

    PasswordInputWidget() {
        super(null, [:])
        inputType = "password"
    }


}

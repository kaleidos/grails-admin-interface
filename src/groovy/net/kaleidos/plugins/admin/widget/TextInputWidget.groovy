package net.kaleidos.plugins.admin.widget

class TextInputWidget extends InputWidget{

    TextInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "text"
    }

    TextInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "text"
    }

    TextInputWidget() {
        super(null, [:])
        inputType = "text"
    }


}

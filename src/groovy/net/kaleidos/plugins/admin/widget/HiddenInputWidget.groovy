package net.kaleidos.plugins.admin.widget

class HiddenInputWidget extends InputWidget{

    HiddenInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "hidden"
    }

    HiddenInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "hidden"
    }

    HiddenInputWidget() {
        super(null, [:])
        inputType = "hidden"
    }


}

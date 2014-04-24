package net.kaleidos.plugins.admin.widget

class HiddenInputWidget extends InputWidget{

    HiddenInputWidget(String value, Map<String, String> attrs=[:]) {
        super(value, attrs)
        inputType = "hidden"
    }

    HiddenInputWidget(Map<String, String> attrs) {
        super(null, attrs)
        inputType = "hidden"
    }

    HiddenInputWidget() {
        super(null, [:])
        inputType = "hidden"
    }


}

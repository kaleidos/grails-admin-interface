package net.kaleidos.plugins.admin.widget

class NumberInputWidget extends InputWidget{

    NumberInputWidget(String value, Map<String, String> attrs=[:]) {
        super(value, attrs)
        inputType = "number"
    }

    NumberInputWidget(Map<String, String> attrs) {
        super(null, attrs)
        inputType = "number"
    }

    NumberInputWidget() {
        super(null, [:])
        inputType = "number"
    }


}

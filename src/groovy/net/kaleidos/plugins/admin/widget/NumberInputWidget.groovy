package net.kaleidos.plugins.admin.widget

class NumberInputWidget extends InputWidget{

    NumberInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "number"
    }

    NumberInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "number"
    }

    NumberInputWidget() {
        super(null, [:])
        inputType = "number"
    }

    @Override
    String renderAsJson() {
        return value
    }

}

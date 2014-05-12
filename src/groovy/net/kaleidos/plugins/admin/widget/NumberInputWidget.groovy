package net.kaleidos.plugins.admin.widget

class NumberInputWidget extends InputWidget{

    NumberInputWidget() {
        inputType = "number"
    }

    @Override
    String renderAsJson() {
        return value
    }

}

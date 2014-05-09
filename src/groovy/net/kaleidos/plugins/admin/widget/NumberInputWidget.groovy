package net.kaleidos.plugins.admin.widget

class NumberInputWidget extends InputWidget{

    NumberInputWidget() {
        inputType = "number"
    }

    @Override
    def getValueForJson() {

        return value
    }

}

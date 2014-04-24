package net.kaleidos.plugins.admin.widget

class TextInputWidget extends InputWidget{

    TextInputWidget(String value, Map<String, String> attrs=[:]) {
        super(value, attrs)
        inputType = "text"
    }

    TextInputWidget(Map<String, String> attrs) {
        super(null, attrs)
        inputType = "text"
    }

    TextInputWidget() {
        super(null, [:])
        inputType = "text"
    }


}

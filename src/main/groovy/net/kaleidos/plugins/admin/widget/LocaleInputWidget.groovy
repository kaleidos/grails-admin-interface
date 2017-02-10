package net.kaleidos.plugins.admin.widget

class LocaleInputWidget extends InputWidget{

    LocaleInputWidget() {
        inputType = "text"
    }

    public void updateValue() {
        updateValue(new Locale(value))
    }


}

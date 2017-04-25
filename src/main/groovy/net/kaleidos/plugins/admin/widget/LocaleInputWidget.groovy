package net.kaleidos.plugins.admin.widget

import groovy.transform.CompileStatic

@CompileStatic
class LocaleInputWidget extends InputWidget {

    LocaleInputWidget() {
        inputType = "text"
    }

    public void updateValue() {
        updateValue(new Locale((String)value))
    }


}

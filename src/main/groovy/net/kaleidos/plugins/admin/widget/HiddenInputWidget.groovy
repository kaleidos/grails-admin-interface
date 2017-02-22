package net.kaleidos.plugins.admin.widget

import groovy.transform.CompileStatic

@CompileStatic
class HiddenInputWidget extends InputWidget {

    HiddenInputWidget() {
        inputType = "hidden"
    }

}

package net.kaleidos.plugins.admin.widget

import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode

@CompileStatic
class DateTimeInputWidget extends InputWidget {

    DateTimeInputWidget() {
        inputType = "datetime"
    }

    @Override
    @CompileStatic(TypeCheckingMode.SKIP)
    List<Map> getAssets() {
        List<String> results = [
            'libs/bootstrap-datepicker/css/datepicker3.css',
            'libs/bootstrap-datepicker/js/bootstrap-datepicker.js'
        ]

        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }
}

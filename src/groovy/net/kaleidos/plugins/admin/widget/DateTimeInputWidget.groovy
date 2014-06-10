package net.kaleidos.plugins.admin.widget

class DateTimeInputWidget extends InputWidget{

    DateTimeInputWidget() {
        inputType = "datetime"
    }

    List<String> getAssets() {
        def results = [
            'libs/bootstrap-datepicker/css/datepicker3.css',
            'libs/bootstrap-datepicker/js/bootstrap-datepicker.js'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }
}

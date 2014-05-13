package net.kaleidos.plugins.admin.widget

class DateTimeInputWidget extends InputWidget{

    DateTimeInputWidget() {
        inputType = "datetime"
    }

    List<String> getAssets() {
        return [ 'libs/bootstrap-datepicker/css/datepicker3.css', 'libs/bootstrap-datepicker/js/bootstrap-datepicker.js']
    }
}

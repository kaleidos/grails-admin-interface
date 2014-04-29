package net.kaleidos.plugins.admin.widget

class UrlInputWidget extends InputWidget{

    UrlInputWidget(String value, Map attrs=[:]) {
        super(value, attrs)
        inputType = "url"
    }

    UrlInputWidget(Map attrs) {
        super(null, attrs)
        inputType = "url"
    }

    UrlInputWidget() {
        super(null, [:])
        inputType = "url"
    }


}

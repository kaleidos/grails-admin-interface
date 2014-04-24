package net.kaleidos.plugins.admin.widget

class UrlInputWidget extends InputWidget{

    UrlInputWidget(String value, Map<String, String> attrs=[:]) {
        super(value, attrs)
        inputType = "url"
    }

    UrlInputWidget(Map<String, String> attrs) {
        super(null, attrs)
        inputType = "url"
    }

    UrlInputWidget() {
        super(null, [:])
        inputType = "url"
    }


}

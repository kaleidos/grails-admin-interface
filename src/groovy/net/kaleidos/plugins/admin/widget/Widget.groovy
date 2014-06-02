package net.kaleidos.plugins.admin.widget

abstract class Widget {
    Map htmlAttrs = [:]
    Map internalAttrs = [:]
    def value

    abstract String render()

    String renderBeforeForm() {}
    String renderAfterForm() {}

    def getValueForJson() {
        return ((value!=null)?value.toString():'')
    }
    List<String> getAssets() {
        return []
    }

    public void updateValue() {
        updateValue(value)
    }

    def updateValue(value) {
        if (internalAttrs['emptyIsNull'] && value == "") {
            value = null
        }
        internalAttrs["domainObject"]."${internalAttrs['propertyName']}" = value
    }

    public String renderError(Throwable t) {
        log.error t.message
        return "<p style='color:red'>${t?t.message:'ERROR'}</p>"
    }
}

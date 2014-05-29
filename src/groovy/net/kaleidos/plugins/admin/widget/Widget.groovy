package net.kaleidos.plugins.admin.widget

abstract class Widget {
    Map htmlAttrs = [:]
    Map internalAttrs = [:]
    def value

    abstract String render()

    String renderBeforeForm() {}
    String renderAfterForm() {}

    def getValueForJson() {
        return value.toString()
    }
    List<String> getAssets() {
        return []
    }

    public void updateValue() {
        updateValue(value)
    }

    def updateValue(value) {
        internalAttrs["domainObject"]."${internalAttrs['propertyName']}" = value
    }

    public String renderError(Throwable t) {
        return "<p style='color:red'>${t.message}</p>"
    }
}

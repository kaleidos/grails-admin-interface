package net.kaleidos.plugins.admin.widget

import groovy.transform.CompileStatic

@CompileStatic
abstract class Widget {
    public static final String DOMAIN_OBJECT_ATTR_NAME = "domainObject"
    public static final String PROPERTY_NAME_ATTR_NAME = "propertyName"
    public static final String DOMAIN_CLASS_NAME_ATTR = "domainClass"

    Map htmlAttrs = [:]
    Map internalAttrs = [:]
    def value

    abstract String render()

    String renderBeforeForm() {}
    String renderAfterForm() {}

    def getValueForJson() {
        return (value != null) ? value.toString() : ''
    }

    List<Map> getAssets() {
        return []
    }

    void updateValue() {
        updateValue(value)
    }

    void updateValue(def value) {
        if (internalAttrs['emptyIsNull'] && value == "") {
            value = null
        }
        def domainInstance = internalAttrs[DOMAIN_OBJECT_ATTR_NAME]
        String propertyName = internalAttrs[PROPERTY_NAME_ATTR_NAME]
        domainInstance[propertyName] = value
    }

    public String renderError(Throwable t) {
        //log.error t.message
        return "<p style='color:red'>${t?t.message:'ERROR'}</p>"
    }

    Class getDomainClass() { return internalAttrs[DOMAIN_CLASS_NAME_ATTR]}
    String getPropertyName() { return internalAttrs[PROPERTY_NAME_ATTR_NAME]}
    def getDomainInstance() { return internalAttrs[DOMAIN_OBJECT_ATTR_NAME]}
}

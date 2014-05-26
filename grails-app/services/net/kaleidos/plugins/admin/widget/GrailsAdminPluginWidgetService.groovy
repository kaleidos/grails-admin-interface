package net.kaleidos.plugins.admin.widget

import net.kaleidos.plugins.admin.DomainInspector
import org.codehaus.groovy.grails.validation.*

class GrailsAdminPluginWidgetService {
    Widget getWidgetForClass(Class clazz, String propertyName, Map customWidget=null, Map attributes=[:]) {
        def inspector = new DomainInspector(clazz)
        def widget = null
        if (customWidget) {
            try {
                def widgetClass = Class.forName(customWidget['class'], true, Thread.currentThread().contextClassLoader)
                widget = widgetClass?.newInstance()
                widget.internalAttrs = customWidget.attributes
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class ${customWidget['class']} not found or not implemented")
            }
        } else {
            widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(clazz, propertyName)
        }

        widget.internalAttrs["grailsDomainClass"] = inspector.domainClass
        widget.internalAttrs["domainClass"] = inspector.clazz
        widget.internalAttrs["propertyName"] = propertyName

        if (attributes) {
            widget.htmlAttrs.putAll(attributes)
        }

        return widget
    }

    Widget getWidget(Object object, String propertyName, Map customWidget=null, Map attributes=[:]) {
        def widget = getWidgetForClass(object.class, propertyName, customWidget, attributes)

        def inspector = new DomainInspector(object)
        def constraints = inspector.getPropertyConstraints(propertyName)

        widget.internalAttrs["domainObject"] = object
        widget.value = _getValueForWidget(object, propertyName)

        widget.htmlAttrs.putAll(["name":propertyName])
        _setAttrsFromConstraints(widget, constraints)
        _setAttrsForRelations(widget, object, propertyName)
        return widget
    }

    void _setAttrsFromConstraints(def widget, def constraints){
        def attrs = [:]
        //Attribs from constraints
        constraints.each{
            _setAttributeFromConstraint(widget, it)
        }
    }

    void _setAttributeFromConstraint(def widget, AbstractConstraint constraint){
        //There is no html constraints for MinSizeConstraint

        if (constraint instanceof MaxConstraint) {
            widget.htmlAttrs.putAll(['max':"${constraint.getMaxValue()}"])
        } else if (constraint instanceof MinConstraint) {
            widget.htmlAttrs.putAll(['min':"${constraint.getMinValue()}"])
        } else if (constraint instanceof RangeConstraint) {
            widget.htmlAttrs.putAll(['max':"${constraint.getRange().to}",
                    'min':"${constraint.getRange().from}"])
        } else if (constraint instanceof MaxSizeConstraint) {
            widget.htmlAttrs.putAll(['maxlength':"${constraint.getMaxSize()}"])
        } else if (constraint instanceof NullableConstraint) {
            //TODO: Also BlankConstraint??
            if (!constraint.isNullable()) {
                widget.htmlAttrs.putAll(['required':"true"])
            }
        } else if (constraint instanceof InListConstraint) {
            def options = [:]
            constraint.list.each {
                options[it] = it
            }
            widget.internalAttrs.putAll(['options':options])
        }
    }

    def _setAttrsForRelations(def widget, def object, def propertyName){
        def inspector = new DomainInspector(object)
        if (inspector.isOneToMany(propertyName) || inspector.isManyToMany(propertyName)){
            def relatedClass = inspector.getPropertyDomainClass(propertyName)
            def relatedInspector = new DomainInspector(relatedClass)
            widget.internalAttrs["relatedDomainClass"] = relatedInspector.getClazz()
        } else if (inspector.isOneToOne(propertyName) || inspector.isManyToOne(propertyName)) {
            widget.internalAttrs["relatedDomainClass"] = inspector.getPropertyClass(propertyName)
        }
    }

    def _getValueForWidget(def object, def propertyName){
        def inspector = new DomainInspector(object)
        def value = object."${propertyName}"

        if (value) {
            if (inspector.isDomainClass(propertyName)) {
                //It is a domain class
                value = value.id
            } else if (inspector.isOneToMany(propertyName) || inspector.isManyToMany(propertyName)){
                return value*.id
            }

            return value
        }
        return null
    }
}

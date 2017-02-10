package net.kaleidos.plugins.admin.widget

import grails.validation.AbstractConstraint
import net.kaleidos.plugins.admin.DomainInspector
import org.codehaus.groovy.grails.validation.*
import org.grails.datastore.gorm.validation.constraints.InListConstraint
import org.grails.datastore.gorm.validation.constraints.MaxConstraint
import org.grails.datastore.gorm.validation.constraints.MaxSizeConstraint
import org.grails.datastore.gorm.validation.constraints.MinConstraint
import org.grails.datastore.gorm.validation.constraints.NullableConstraint
import org.grails.datastore.gorm.validation.constraints.RangeConstraint

class GrailsAdminPluginWidgetService {
    Widget getWidgetForClass(Class clazz, String propertyName, Map customWidget=null, Map attributes=[:]) {
        def inspector = new DomainInspector(clazz)
        def widget = null
        if (customWidget) {
            if (customWidget['class']) {
                widget = _instanciateCustomWidget(customWidget['class'], customWidget.attributes)
            } else {
                widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(clazz, propertyName)
                if (customWidget.attributes) {
                    widget.internalAttrs.putAll(customWidget.attributes)
                }
            }
        } else {
            widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(clazz, propertyName)
        }

        widget.internalAttrs["grailsDomainClass"] = inspector.domainClass
        widget.internalAttrs["domainClass"] = inspector.clazz
        widget.internalAttrs["propertyName"] = propertyName

        _setAttrsForRelations(widget, inspector, propertyName)
        def constraints = inspector.getPropertyConstraints(propertyName)
        _setAttrsFromConstraints(widget, constraints)

        if (attributes) {
            widget.htmlAttrs.putAll(attributes)
        }

        widget.htmlAttrs.name = propertyName

        return widget
    }

    def _instanciateCustomWidget(String clazz, Map attributes) {
        def widget
        def widgetClass

        try {
            widgetClass = Class.forName(clazz, true, Thread.currentThread().contextClassLoader)
        } catch (ClassNotFoundException e) {
            try {
                widgetClass = Class.forName("net.kaleidos.plugins.admin.widget.$clazz", true, Thread.currentThread().contextClassLoader)
            } catch (ClassNotFoundException e2) {
                throw new RuntimeException("Class ${customWidget['class']} not found or not implemented")
            }
        }
        widget = widgetClass?.newInstance()
        widget.internalAttrs = attributes
        return widget
    }

    Widget getWidget(Object object, String propertyName, Map customWidget=null, Map attributes=[:]) {
        def widget = getWidgetForClass(object.class, propertyName, customWidget, attributes)

        widget.internalAttrs["domainObject"] = object
        widget.value = _getValueForWidget(object, propertyName)


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

    def _setAttrsForRelations(def widget, def inspector, def propertyName){
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

        if (value!=null) {
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

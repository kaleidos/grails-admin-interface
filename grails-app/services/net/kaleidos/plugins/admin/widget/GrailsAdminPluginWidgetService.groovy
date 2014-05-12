package net.kaleidos.plugins.admin.widget
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

import org.codehaus.groovy.grails.validation.CreditCardConstraint
import org.codehaus.groovy.grails.validation.EmailConstraint
import org.codehaus.groovy.grails.validation.InListConstraint
import org.codehaus.groovy.grails.validation.MatchesConstraint
import org.codehaus.groovy.grails.validation.MaxConstraint
import org.codehaus.groovy.grails.validation.MaxSizeConstraint
import org.codehaus.groovy.grails.validation.MinConstraint
import org.codehaus.groovy.grails.validation.MinSizeConstraint
import org.codehaus.groovy.grails.validation.NotEqualConstraint
import org.codehaus.groovy.grails.validation.NullableConstraint
import org.codehaus.groovy.grails.validation.BlankConstraint
import org.codehaus.groovy.grails.validation.RangeConstraint
import org.codehaus.groovy.grails.validation.ScaleConstraint
import org.codehaus.groovy.grails.validation.SizeConstraint
import org.codehaus.groovy.grails.validation.UrlConstraint
import org.codehaus.groovy.grails.validation.ValidatorConstraint
import org.codehaus.groovy.grails.validation.AbstractConstraint
import org.springframework.beans.factory.NoSuchBeanDefinitionException

import net.kaleidos.plugins.admin.widget.relation.RelationSelectWidget
import net.kaleidos.plugins.admin.widget.relation.RelationSelectMultipleWidget


import org.springframework.util.ClassUtils

class GrailsAdminPluginWidgetService {
    def grailsApplication

    DefaultGrailsDomainClass getGrailsDomainClass(Object object) {
        //return new DefaultGrailsDomainClass(object.class)
        try {
            def className = ClassUtils.getUserClass(object.getClass()).name
            return grailsApplication.mainContext.getBean("${className}DomainClass")
        } catch (NoSuchBeanDefinitionException e){
            return null
        }
    }

    DefaultGrailsDomainClass getGrailsDomainClass(Class c) {
        try{
            return grailsApplication.mainContext.getBean("${c.name}DomainClass")
        } catch (NoSuchBeanDefinitionException e){
            return null
        }
    }

    Widget getWidget(Object object, String propertyName, String customWidget=null, Map attributes=[:]) {
        def widget
        def grailsDomainClass = getGrailsDomainClass(object)
        def property = grailsDomainClass.getPropertyByName(propertyName)

        if (!property) {
            throw new RuntimeException("$propertyName not exists in ${object.class}")
        }

        def constraints


        constraints = grailsDomainClass.constrainedProperties.get(propertyName)?.getAppliedConstraints()
        constraints = constraints?:[]


        if (customWidget) {
            try {
                def widgetClass = this.getClass().classLoader.loadClass( customWidget, true, false )
                widget = widgetClass?.newInstance()
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class $customWidget not found or not implemented")
            }
        } else {
            widget = _getDefaultWidgetForType(property.getType(), constraints)
        }


        widget.value = _getValueForWidget(object, property)


        widget.htmlAttrs.putAll(["name":propertyName])
        _setAttrsFromConstraints(widget, constraints)
        _setAttrsForRelations(widget, property)

        //Preference for user-defined attributes
        if (attributes) {
            widget.htmlAttrs.putAll(attributes)
        }

        return widget
    }




    Widget _getDefaultWidgetForType(def type, def constraints){

        def widget
        def constraintsClasses = constraints*.class

        if (InListConstraint.class in constraintsClasses){
            widget = new SelectWidget()
        } else {
            switch ( type ) {
                case [Byte, Short, Integer, Long, Float, Double]:
                    widget = new NumberInputWidget()
                    break
                case [Character, String]:
                    if (EmailConstraint.class in constraintsClasses) {
                        widget = new EmailInputWidget()
                    } else if (UrlConstraint.class in constraintsClasses){
                        widget = new UrlInputWidget()
                    } else {
                        widget = new TextInputWidget()
                    }
                break
                case Date:
                    widget = new DateInputWidget()
                    break
                // case DateTime:
                //     widget = new DateTimeInputWidget()
                //     break
                case Set:
                    widget = new RelationSelectMultipleWidget()
                    break
                default:
                    //It is another domain class?
                    def domain = getGrailsDomainClass(type)
                    if (domain) {
                        widget = new RelationSelectWidget()
                        widget.internalAttrs["relatedDomainClass"] = domain
                    } else {
                        widget = new TextInputWidget()
                    }
            }
        }
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

    def _setAttrsForRelations(def widget, def property){

        if (property.isOneToMany()){
            widget.internalAttrs["relatedDomainClass"] = property.getReferencedDomainClass()
        }
    }


    def _getValueForWidget(def object, def property){

        def value = object."${property.name}"

        if (value) {
            if (grailsApplication.isDomainClass(property.getType())) {
                //It is a domain class
                value = value.id
            } else if (property.isOneToMany()){
                return value*.id
            }
            return value as String
        }
        return null

    }


}

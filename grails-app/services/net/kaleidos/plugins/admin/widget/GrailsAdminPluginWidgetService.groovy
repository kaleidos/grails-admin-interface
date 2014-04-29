package net.kaleidos.plugins.admin.widget
import org.codehaus.groovy.grails.commons.ApplicationHolder
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



class GrailsAdminPluginWidgetService {
    def grailsApplication = ApplicationHolder.application

    DefaultGrailsDomainClass getGrailsDomainClass(Object object) {
        //return new DefaultGrailsDomainClass(object.class)
        return grailsApplication.mainContext.getBean("${object.class.name}DomainClass")
    }

    Widget getWidget(Object object, String propertyName, String customWidget, Map<String, String> attributes) {
        def widget
        def attrs = [:]


        def grailsDomainClass = getGrailsDomainClass(object)
        def property = object.metaClass.getProperties().find{it.name == propertyName}
        def constraints = grailsDomainClass.constrainedProperties.get(propertyName, null).getAppliedConstraints()


        if (customWidget) {
            def widgetClass = this.getClass().classLoader.loadClass( customWidget, true, false )
            widget = widgetClass?.newInstance()
        } else {
            widget = getDefaultWidgetForType(property.getType(), constraints)
        }


        //Add special properties for some widgets
        //Select
        if (widget instanceof SelectWidget){
            def options = [:]

            def inListConstraint = constraints.find { it instanceof InListConstraint }
            inListConstraint.list.each {
                options[it] = it
            }

            def nullableConstraint = constraints.find { it instanceof NullableConstraint }
            if (nullableConstraint) {
                widget.nullable = nullableConstraint.isNullable()
            }

            widget.options = options
        }


        //Attribs from constraints
        constraints.each{
            def attr = getAttributeFromConstraint(it)
            if (attr) {
                attrs.putAll(attr)
            }
        }

        def value = object."${propertyName}"
        widget.value = value?"${value}":null
        widget.attrs = attrs
        widget.attrs.name = propertyName

        if (attributes) {
            //Preference for user-defined attributes
            widget.attrs.putAll(attributes)
        }

        return widget

    }


    Widget getDefaultWidgetForType(def type, def constraints){
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
                default:
                    widget = new TextInputWidget()
            }
        }
        return widget
    }


    Map getAttributeFromConstraint(AbstractConstraint constraint){
        //There is no html constraints for MinSizeConstraint

        if (constraint instanceof MaxConstraint) {
            return ['max':"${constraint.getMaxValue()}"]
        } else if (constraint instanceof MinConstraint) {
            return ['min':"${constraint.getMinValue()}"]
        } else if (constraint instanceof RangeConstraint) {
            return ['max':"${constraint.getRange().to}",
                    'min':"${constraint.getRange().from}"]
        } else if (constraint instanceof MaxSizeConstraint) {
            return ['maxlength':"${constraint.getMaxSize()}"]
        } else if (constraint instanceof NullableConstraint) {
            //TODO: Also BlankConstraint??
            if (!constraint.isNullable()) {
                return ['required':"true"]
            }
        }
    }



}

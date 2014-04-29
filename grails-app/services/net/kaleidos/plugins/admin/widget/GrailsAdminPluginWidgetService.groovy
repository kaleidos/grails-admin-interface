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



class GrailsAdminPluginWidgetService {
    def grailsApplication

    DefaultGrailsDomainClass getGrailsDomainClass(Object object) {
        //return new DefaultGrailsDomainClass(object.class)
        return grailsApplication.mainContext.getBean("${object.class.name}DomainClass")
    }

    Widget getWidget(Object object, String propertyName, String customWidget, Map attributes) {
        def widget
        def grailsDomainClass = getGrailsDomainClass(object)
        def property = object.metaClass.getProperties().find{it.name == propertyName}
        def constraints = grailsDomainClass.constrainedProperties.get(propertyName, null).getAppliedConstraints()

        if (customWidget) {
            def widgetClass = this.getClass().classLoader.loadClass( customWidget, true, false )
            widget = widgetClass?.newInstance()
        } else {
            widget = _getDefaultWidgetForType(property.getType(), constraints)
        }

        def value = object."${propertyName}"
        widget.value = value?"${value}":null

        widget.attrs = ["name":propertyName]
        widget.attrs.putAll(_getAttrsFromConstraints(constraints))

        //Preference for user-defined attributes
        if (attributes) {
            widget.attrs.putAll(attributes)
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
                default:
                    widget = new TextInputWidget()
            }
        }
        return widget
    }

    Map _getAttrsFromConstraints(def constraints){
        def attrs = [:]
        //Attribs from constraints
        constraints.each{
            def attr = _getAttributeFromConstraint(it)
            if (attr) {
                attrs.putAll(attr)
            }
        }
        return attrs
    }


    Map _getAttributeFromConstraint(AbstractConstraint constraint){
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
        } else if (constraint instanceof InListConstraint) {
            def options = [:]
            constraint.list.each {
                options[it] = it
            }
            return ['options':options]
        }
    }



}

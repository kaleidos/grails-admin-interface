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
import org.codehaus.groovy.grails.validation.RangeConstraint
import org.codehaus.groovy.grails.validation.ScaleConstraint
import org.codehaus.groovy.grails.validation.SizeConstraint
import org.codehaus.groovy.grails.validation.UrlConstraint
import org.codehaus.groovy.grails.validation.ValidatorConstraint



class GrailsAdminPluginWidgetService {
    def grailsApplication = ApplicationHolder.application

    DefaultGrailsDomainClass getGrailsDomainClass(Object object) {
        //return new DefaultGrailsDomainClass(object.class)
        return grailsApplication.mainContext.getBean("${object.class.name}DomainClass")
    }

    Widget getWidget(Object object, String propertyName, String customWidget, Map<String, String> attributes) {
        def widgetClass
        def property = object.metaClass.getProperties().find{it.name == propertyName}

        if (customWidget) {
            widgetClass = this.getClass().classLoader.loadClass( customWidget, true, false )
        } else {
            //Get property type
            def grailsDomainClass = getGrailsDomainClass(object)
            def constraints = grailsDomainClass.constrainedProperties.get(propertyName, null).getAppliedConstraints()
            def constraintsClasses = constraints*.class
            def type = property.getType()

            switch ( type ) {
                case [Byte, Short, Integer, Long, Float, Double]:
                    widgetClass = NumberInputWidget.class
                    break
                case [Character, String]:
                    if (EmailConstraint.class in constraintsClasses) {
                        widgetClass = EmailInputWidget.class
                    } else {
                        widgetClass = TextInputWidget.class
                    }
                    break
                default:
                    widgetClass = TextInputWidget.class
            }
        }

        def widget = widgetClass?.newInstance()

        widget.value = object."${propertyName}"



        return widget

    }



}

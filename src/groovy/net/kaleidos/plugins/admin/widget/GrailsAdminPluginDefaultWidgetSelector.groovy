package net.kaleidos.plugins.admin.widget

import org.codehaus.groovy.grails.validation.ConstrainedProperty
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClassProperty
import org.codehaus.groovy.grails.validation.InListConstraint
import org.codehaus.groovy.grails.validation.UrlConstraint
import org.codehaus.groovy.grails.validation.EmailConstraint
import net.kaleidos.plugins.admin.widget.relation.RelationSelectMultipleWidget
import net.kaleidos.plugins.admin.widget.relation.RelationTableWidget
import net.kaleidos.plugins.admin.widget.relation.RelationPopupOneWidget
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import net.kaleidos.plugins.admin.DomainInspector


class GrailsAdminPluginDefaultWidgetSelector {

    static Widget getDefaultWidgetForProperty(Class clazz, String propertyName){
        def widget
        def inspector = new DomainInspector(clazz)
        def constraints = inspector.getPropertyConstraints(propertyName)
        def constraintsClasses = constraints*.class
        def type = inspector.getPropertyClass(propertyName)

        if (InListConstraint.class in constraintsClasses){
            widget = new SelectWidget()
        } else {
            switch ( type ) {
                case [Byte, Short, Integer, Long]:
                    widget = new NumberInputWidget()
                    break
                case [Float, Double]:
                    widget = new DecimalInputWidget()
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
                case Boolean:
                    widget = new CheckboxInputWidget()
                    break
                case Enum:
                    widget = new EnumWidget()
                    break
                case File:
                    widget = new LabelWidget()
                    break
                case Locale:
                    widget = new LocaleInputWidget()
                    break
                case Collection:
                    if (inspector.isAssociation(propertyName)) {
                        widget = new RelationTableWidget()
                    } else {
                        widget = new LabelWidget()
                    }
                    break
                default:
                    //Is it another domain class?
                    if (inspector.isDomain(type)) {
                        widget = new RelationPopupOneWidget()
                    } else {
                        widget = new LabelWidget()
                    }
            }
        }
        return widget
    }

}

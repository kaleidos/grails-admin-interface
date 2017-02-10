package net.kaleidos.plugins.admin.widget

import net.kaleidos.plugins.admin.DomainInspector
import net.kaleidos.plugins.admin.widget.relation.RelationPopupOneWidget
import net.kaleidos.plugins.admin.widget.relation.RelationTableWidget
import org.grails.datastore.gorm.validation.constraints.EmailConstraint
import org.grails.datastore.gorm.validation.constraints.InListConstraint
import org.grails.datastore.gorm.validation.constraints.UrlConstraint

class GrailsAdminPluginDefaultWidgetSelector {
    static String[] FORBIDEN_PROPERTIES = ['id','version', 'dateCreated', 'lastUpdated']

    static Widget getDefaultWidgetForProperty(Class clazz, String propertyName){
        def widget
        def inspector = new DomainInspector(clazz)
        def constraints = inspector.getPropertyConstraints(propertyName)
        def constraintsClasses = constraints*.class
        def type = inspector.getPropertyClass(propertyName)

        if (propertyName in FORBIDEN_PROPERTIES){
            widget = new LabelWidget()
        } else if (InListConstraint.class in constraintsClasses){
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

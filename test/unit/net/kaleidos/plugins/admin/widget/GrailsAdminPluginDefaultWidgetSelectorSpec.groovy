package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import admin.test.AdminDomainTest
import admin.test.TestDomainRelationOne
import admin.test.TestDomain
import admin.test.TestDomainRelation
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

import net.kaleidos.plugins.admin.widget.relation.RelationTableWidget
import net.kaleidos.plugins.admin.widget.relation.RelationPopupOneWidget

import grails.util.Holders


@Mock([AdminDomainTest, TestDomainRelation, TestDomainRelationOne, TestDomain])
class GrailsAdminPluginDefaultWidgetSelectorSpec extends Specification {
    def setupSpec() {
        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            AdminDomainTest.class,
            TestDomainRelation.class,
            TestDomainRelationOne.class,
            TestDomain.class
        ] as Class[])
    }

    void 'get widget for string inline attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)

        then:
            widget.class == SelectWidget.class

        where:
            propertyName = "country"
    }

    void 'get widget for long attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)

        then:
            widget.class == NumberInputWidget.class

        where:
            propertyName = "longNumber"
    }

    void 'get widget for string attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)

        then:
            widget.class == TextInputWidget.class
        where:
            propertyName = "name"
    }

    void 'get widget for string email attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)
        then:
            widget.class == EmailInputWidget.class
        where:
            propertyName = "email"
    }

    void 'get widget for string url attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)
        then:
            widget.class == UrlInputWidget.class
        where:
            propertyName = "web"
    }

    void 'get widget for date attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)
        then:
            widget.class == DateInputWidget.class
        where:
            propertyName = "birthday"
    }

    void 'get widget for boolean attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)
        then:
            widget.class == CheckboxInputWidget.class
        where:
            propertyName = "ok"
    }

    void 'get widget for unknown type attribute'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(AdminDomainTest, propertyName)
        then:
            widget.class == TextInputWidget.class
        where:
            propertyName = "locale"
    }

    void 'get widget for one to one relation'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(TestDomainRelationOne, propertyName)
        then:
            widget.class == RelationPopupOneWidget.class
        where:
            propertyName = "testDomain"
    }

    void 'get widget for one to n relation'() {
        when:
            def widget = GrailsAdminPluginDefaultWidgetSelector.getDefaultWidgetForProperty(TestDomainRelation, propertyName)
        then:
            widget.class == RelationTableWidget.class
        where:
            propertyName = "testDomain"
    }
}

package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import admin.test.AdminDomainTest
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass
import org.codehaus.groovy.grails.commons.ApplicationHolder


@Mock([AdminDomainTest])
class GrailsAdminPluginWidgetServiceSpec extends Specification {
    AdminDomainTest adminDomainTest
    def widgetService

    void setup() {
        adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        widgetService = new GrailsAdminPluginWidgetService()
        def grailsApplication = ApplicationHolder.application

        //on test enviroment the is not loaded beans on mainContext, so we need to use a custom function
        widgetService.metaClass.getGrailsDomainClass = {Object object ->
            return new DefaultGrailsDomainClass(object.class)
        }


    }

    void 'get widget for long class'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "longNumber", null, null)
        then:
            widget.class == NumberInputWidget.class
    }

    void 'get widget for integer class with attribs (not nullable, min, max, required)'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "age", null, null)
        then:
            widget.class == NumberInputWidget.class
            widget.value == '25'
            widget.attrs.min == '18'
            widget.attrs.max == '100'
            widget.attrs.required == 'true'
            widget.attrs.name == 'age'
    }

    void 'get widget for integer class with attribs (range)'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "year", null, null)
        then:
            widget.class == NumberInputWidget.class
            widget.value == null
            widget.attrs.min == '2014'
            widget.attrs.max == '2020'
            widget.attrs.name == 'year'
    }

    void 'get widget for string class with attribs (maxsize)'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "surname", null, null)
        then:
            widget.class == TextInputWidget.class
            widget.value == null
            widget.attrs.maxlength == '100'
    }

    void 'get widget for plain string class'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "name", null, null)
        then:
            widget.class == TextInputWidget.class
    }

    void 'get widget for email string class'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "email", null, null)
        then:
            widget.class == EmailInputWidget.class
    }

    void 'get widget for url string class'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "web", null, null)
        then:
            widget.class == UrlInputWidget.class
    }

    void 'get widget for inList string class'() {
        when:
            def widget = widgetService.getWidget(adminDomainTest, "country", null, null)
        then:
            widget.class == SelectWidget.class
            widget.attrs.options == ["Canada":"Canada", "Spain":"Spain", "USA":"USA"]
    }



}

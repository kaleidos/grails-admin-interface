package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class DateTimeInputWidgetSpec extends Specification {
	
    void 'create input datetime without value nor attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget()

        when:
            def html = dateTimeInputWidget.render()

        then:
            html == "<input type=\"datetime\" />"
    }


    void 'create input datetime with value without attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget(value)
            
        when:
            def html = dateTimeInputWidget.render()

        then:
            html == "<input type=\"datetime\" value=\"${value}\" />"
            
        where:
            value = "1234"
    }

    void 'create input dateTime without value with attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget(attrs)

        when:
            def html = dateTimeInputWidget.render()

        then:
            html == "<input type=\"datetime\" size=\"10\" name=\"datetime\" />"
            
        where:
            attrs = ['size':10, 'name': 'datetime']
    }


    void 'create input datetime with value and attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget(value, attrs)

        when:
            def html = dateTimeInputWidget.render()

        then:
            html == "<input type=\"datetime\" value=\"1234\" size=\"10\" name=\"datetime\" />"
            
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'datetime']
    }
}

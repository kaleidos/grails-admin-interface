package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class DateInputWidgetSpec extends Specification {
	
    void 'create input text without value nor attribs'() {
        setup:
            def textInputWidget = new DateInputWidget()

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"date\" />"
    }


    void 'create input text with value without attribs'() {
        setup:
            def textInputWidget = new DateInputWidget(value)
            
        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"date\" value=\"${value}\" />"
            
        where:
            value = "1234"
    }

    void 'create input text without value with attribs'() {
        setup:
            def textInputWidget = new DateInputWidget(attrs)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"date\" size=\"10\" name=\"test\" />"
            
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input text with value and attribs'() {
        setup:
            def textInputWidget = new DateInputWidget(value, attrs)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"date\" value=\"1234\" size=\"10\" name=\"test\" />"
            
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

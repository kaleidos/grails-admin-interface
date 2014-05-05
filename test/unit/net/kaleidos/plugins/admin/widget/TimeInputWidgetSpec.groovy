package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class TimeInputWidgetSpec extends Specification {
    void 'create input time without value nor attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget()

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" />"
    }


    void 'create input time with value without attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(value)

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input time without value with attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(attrs)

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" size=\"10\" name=\"time\" />"
        where:
            attrs = ['size':10, 'name': 'time']
    }


    void 'create input time with value and attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(value, attrs)

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" value=\"1234\" size=\"10\" name=\"time\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'time']
    }
}

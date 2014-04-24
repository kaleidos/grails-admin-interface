package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class NumberInputWidgetSpec extends Specification {
    void 'create input number without value nor attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget()

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" />"
    }


    void 'create input number with value without attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(value)

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input number without value with attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(attrs)

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input number with value and attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(value, attrs)

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" value=\"1234\" size=\"10\" name=\"test\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

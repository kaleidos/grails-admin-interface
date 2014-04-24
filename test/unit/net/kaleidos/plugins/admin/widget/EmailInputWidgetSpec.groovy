package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class EmailInputWidgetSpec extends Specification {
    void 'create input email without value nor attribs'() {
        setup:
            def emailInputWidget = new EmailInputWidget()

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" />"
    }


    void 'create input email with value without attribs'() {
        setup:
            def emailInputWidget = new EmailInputWidget(value)

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input email without value with attribs'() {
        setup:
            def emailInputWidget = new EmailInputWidget(attrs)

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input email with value and attribs'() {
        setup:
            def emailInputWidget = new EmailInputWidget(value, attrs)

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" value=\"1234\" size=\"10\" name=\"test\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

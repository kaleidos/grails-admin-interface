package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class PasswordInputWidgetSpec extends Specification {
    void 'create input password without value nor attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget()

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" />"
    }


    void 'create input password with value without attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(value)

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input password without value with attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(attrs)

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input password with value and attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(value, attrs)

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" value=\"1234\" size=\"10\" name=\"test\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

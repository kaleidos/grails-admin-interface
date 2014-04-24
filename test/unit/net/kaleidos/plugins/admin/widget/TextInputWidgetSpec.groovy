package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class TextInputWidgetSpec extends Specification {
    void 'create input text without value nor attribs'() {
        setup:
            def textInputWidget = new TextInputWidget()

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" />"
    }


    void 'create input text with value without attribs'() {
        setup:
            def textInputWidget = new TextInputWidget(value)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input text without value with attribs'() {
        setup:
            def textInputWidget = new TextInputWidget(attrs)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input text with value and attribs'() {
        setup:
            def textInputWidget = new TextInputWidget(value, attrs)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" value=\"1234\" size=\"10\" name=\"test\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

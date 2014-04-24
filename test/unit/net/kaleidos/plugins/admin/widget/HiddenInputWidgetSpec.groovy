package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class HiddenInputWidgetSpec extends Specification {
    void 'create input hidden without value nor attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget()

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" />"
    }


    void 'create input hidden with value without attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(value)

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input hidden without value with attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(attrs)

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input hidden with value and attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(value, attrs)

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" value=\"1234\" size=\"10\" name=\"test\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class CheckboxInputWidgetSpec extends Specification {
    void 'create input checkbox without value nor attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget()

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\"></input>"
    }

    @Unroll
    void 'create input checkbox with value and text:#text and without attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, null)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\" value=\"${value}\">${text?:''}</input>"

        where:
            value = "1234"
            text << ["el valor es 1234", null]
    }

    @Unroll
    void 'create input checkbox without value and text:#text and without attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, null)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\">${text?:''}</input>"

        where:
            value = null
            text << ["el valor es 1234", null]
    }

    @Unroll
    void 'create input checkbox with value and text:#text and with attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, attrs)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\" value=\"${value}\" size=\"10\" name=\"test\">${text?:''}</input>"

        where:
            attrs = ['size':10, 'name': 'test']
            value = "1234"
            text << ["el valor es 1234", null]
    }

    @Unroll
    void 'create input checkbox without value and text:#text and with attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, attrs)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\" size=\"10\" name=\"test\">${text?:''}</input>"

        where:
            attrs = ['size':10, 'name': 'test']
            value = null
            text << ["el valor es 1234", null]
    }
}

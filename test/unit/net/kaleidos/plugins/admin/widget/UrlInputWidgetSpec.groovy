package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class UrlInputWidgetSpec extends Specification {
    void 'create input url without value nor attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget()

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" />"
    }


    void 'create input url with value without attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget(value)

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" value=\"${value}\" />"
        where:
            value = "1234"
    }

    void 'create input url without value with attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget(attrs)

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input url with value and attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget(value, attrs)

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" value=\"1234\" size=\"10\" name=\"test\" />"
        where:
            value = "1234"
            attrs = ['size':10, 'name': 'test']
    }
}

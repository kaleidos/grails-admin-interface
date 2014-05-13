package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class TimeInputWidgetSpec extends Specification {

    void setup() {
        Object.metaClass.encodeAsHTML = {
            def encoder = new HTMLCodec().getEncoder()
            return encoder.encode(delegate)
        }
    }

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
            def timeInputWidget = new TimeInputWidget(value:value)

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input time without value with attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(htmlAttrs:attrs)

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" size=\"10\" name=\"time\" />"
        where:
            attrs = ['size':10, 'name': 'time']
    }


    void 'create input time with value and attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = timeInputWidget.render()

        then:
            html == "<input type=\"time\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"time\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'time']
    }
}

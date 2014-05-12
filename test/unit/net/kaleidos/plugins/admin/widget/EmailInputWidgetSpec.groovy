package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class EmailInputWidgetSpec extends Specification {

    void setup() {
        Object.metaClass.encodeAsHTML = {
            def encoder = new HTMLCodec().getEncoder()
            return encoder.encode(delegate)
        }
    }

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
            def emailInputWidget = new EmailInputWidget(value:value)

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input email without value with attribs'() {
        setup:
            def emailInputWidget = new EmailInputWidget(htmlAttrs:attrs)

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input email with value and attribs'() {
        setup:
            def emailInputWidget = new EmailInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = emailInputWidget.render()

        then:
            html == "<input type=\"email\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

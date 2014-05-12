package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class PasswordInputWidgetSpec extends Specification {

    void setup() {
        Object.metaClass.encodeAsHTML = {
            def encoder = new HTMLCodec().getEncoder()
            return encoder.encode(delegate)
        }
    }

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
            def passwordInputWidget = new PasswordInputWidget(value:value)

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input password without value with attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(htmlAttrs:attrs)

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input password with value and attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = passwordInputWidget.render()

        then:
            html == "<input type=\"password\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class PasswordInputWidgetSpec extends Specification {
    @Shared
    def slurper

    void setupSpec() {
        def parser = new org.cyberneko.html.parsers.SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        slurper = new XmlSlurper(parser)
    }

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
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "password"
    }

    void 'create input password with value without attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(value:value)

        when:
            def html = passwordInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "password"
            result.BODY.INPUT.@value.text() == value.encodeAsHTML()

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input password without value with attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(htmlAttrs:attrs)

        when:
            def html = passwordInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "password"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"

        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input password with value and attribs'() {
        setup:
            def passwordInputWidget = new PasswordInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = passwordInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "password"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@value.text() == value.encodeAsHTML()

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

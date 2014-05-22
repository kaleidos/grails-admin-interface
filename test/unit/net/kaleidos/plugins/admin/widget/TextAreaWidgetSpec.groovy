package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class TextAreaWidgetSpec extends Specification {
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

    void 'create text area without value nor attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget()

        when:
            def html = textAreaWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.TEXTAREA.size() == 1
    }

    void 'create text area with value without attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(value:value)

        when:
            def html = textAreaWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.TEXTAREA.size() == 1
            result.BODY.TEXTAREA.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create text area without value with attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(htmlAttrs:attrs)

        when:
            def html = textAreaWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.TEXTAREA.size() == 1
            result.BODY.TEXTAREA.@rows == "4"
            result.BODY.TEXTAREA.@cols == "50"

        where:
            attrs = ['rows':4, 'cols':50]
    }


    void 'create text area with value and attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(value:value, htmlAttrs:attrs)

        when:
            def html = textAreaWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.TEXTAREA.size() == 1
            result.BODY.TEXTAREA.@rows == "4"
            result.BODY.TEXTAREA.@cols == "50"
            result.BODY.TEXTAREA.text() == value

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['rows':4, 'cols':50]
    }
}

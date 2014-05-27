package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class TimeInputWidgetSpec extends Specification {
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

    void 'create input time without value nor attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget()

        when:
            def html = timeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "time"
    }


    void 'create input time with value without attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(value:value)

        when:
            def html = timeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "time"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input time without value with attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(htmlAttrs:attrs)

        when:
            def html = timeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "time"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "time"

        where:
            attrs = ['size':10, 'name': 'time']
    }


    void 'create input time with value and attribs'() {
        setup:
            def timeInputWidget = new TimeInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = timeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "time"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "time"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'time']
    }
}

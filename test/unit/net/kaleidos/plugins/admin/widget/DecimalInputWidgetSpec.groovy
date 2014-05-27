package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class DecimalInputWidgetSpec extends Specification {
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

    void 'create input number without value nor attribs'() {
        setup:
            def numberInputWidget = new DecimalInputWidget()

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT."@data-parsley-type".text() == "number"
    }

    void 'create input number with value without attribs'() {
        setup:
            def numberInputWidget = new DecimalInputWidget(value:value)

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT."@data-parsley-type".text() == "number"
            result.BODY.INPUT.@value.text() == "$value"

        where:
            value = 1234.11
    }

    void 'create input number without value with attribs'() {
        setup:
            def numberInputWidget = new DecimalInputWidget(htmlAttrs:attrs)

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT."@data-parsley-type".text() == "number"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"

        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input number with value and attribs'() {
        setup:
            def numberInputWidget = new DecimalInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT."@data-parsley-type".text() == "number"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@value.text() == "$value"

        where:
            value = 1234.112
            attrs = ['size':10, 'name': 'test']
    }
}

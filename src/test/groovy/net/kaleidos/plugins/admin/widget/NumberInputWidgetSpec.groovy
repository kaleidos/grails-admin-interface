package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import org.grails.plugins.codecs.HTMLCodec
import spock.lang.*

class NumberInputWidgetSpec extends Specification {
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
            def numberInputWidget = new NumberInputWidget()

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "number"
    }

    void 'create input number with value without attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(value:value)

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "number"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input number without value with attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(htmlAttrs:attrs)

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "number"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"

        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input number with value and attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = numberInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "number"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }


    void 'Update value'() {
        setup:
            def numberInputWidget = new NumberInputWidget()
            numberInputWidget.internalAttrs['domainObject'] = [:]
            numberInputWidget.internalAttrs['propertyName'] = "test"

        when:
            numberInputWidget.value = value?"$value":value
            numberInputWidget.updateValue()

        then:
            numberInputWidget.internalAttrs.domainObject.test == value

        where:
            value << [10, (((long)Integer.MAX_VALUE) + 10), null]
    }

    void 'Update value (fail)'() {
        setup:
            def numberInputWidget = new NumberInputWidget()
            numberInputWidget.internalAttrs['domainObject'] = [:]
            numberInputWidget.internalAttrs['propertyName'] = "test"

        when:
            numberInputWidget.value = "$value"
            numberInputWidget.updateValue()

        then:
            thrown(RuntimeException)

        where:
            value = "NOT A NUMBER"
    }

}

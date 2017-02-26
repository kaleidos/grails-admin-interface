package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import org.grails.plugins.codecs.HTMLCodec
import spock.lang.Shared
import spock.lang.Specification


class HiddenInputWidgetSpec extends Specification {
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

    void 'create input hidden without value nor attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget()

        when:
            def html = hiddenInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "hidden"
    }

    void 'create input hidden with value without attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(value:value)

        when:
            def html = hiddenInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "hidden"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input hidden without value with attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(htmlAttrs:attrs)

        when:
            def html = hiddenInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "hidden"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"

        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input hidden with value and attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = hiddenInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "hidden"
            result.BODY.INPUT.@value.text() == value
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import org.grails.plugins.codecs.HTMLCodec
import spock.lang.*

class MapWidgetSpec extends Specification {
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

    void 'create map without value nor attribs'() {
        setup:
            def mapWidget = new MapWidget()

        when:
            def html = mapWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].SPAN.size() == 1
            result.BODY.DIV.DIV[0].SPAN.IFRAME.size() == 1
            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == "text"
            result.BODY.DIV.DIV[1].INPUT.@value.text() == ""
    }

    void 'create map with value without attribs'() {
        setup:
            def mapWidget = new MapWidget(value:value)

        when:
            def html = mapWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].SPAN.size() == 1
            result.BODY.DIV.DIV[0].SPAN.IFRAME.size() == 1
            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == "text"
            result.BODY.DIV.DIV[1].INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create map without value with attribs'() {
        setup:
            def mapWidget = new MapWidget(htmlAttrs:attrs)

        when:
            def html = mapWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].SPAN.size() == 1
            result.BODY.DIV.DIV[0].SPAN.IFRAME.size() == 1
            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == "text"
            result.BODY.DIV.DIV[1].INPUT.@value.text() == ""
            result.BODY.DIV.DIV[1].INPUT.@size.text() == "10"
            result.BODY.DIV.DIV[1].INPUT.@name.text() == "test"

        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create map with value and attribs'() {
        setup:
            def mapWidget = new MapWidget(value:value, htmlAttrs:attrs)

        when:
            def html = mapWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].SPAN.size() == 1
            result.BODY.DIV.DIV[0].SPAN.IFRAME.size() == 1
            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == "text"
            result.BODY.DIV.DIV[1].INPUT.@value.text() == value
            result.BODY.DIV.DIV[1].INPUT.@size.text() == "10"
            result.BODY.DIV.DIV[1].INPUT.@name.text() == "test"

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

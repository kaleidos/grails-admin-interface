package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import org.grails.plugins.codecs.HTMLCodec
import spock.lang.Shared
import spock.lang.Specification


class DateTimeInputWidgetSpec extends Specification {
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

    void 'create input datetime without value nor attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget()

        when:
            def html = dateTimeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "datetime"
    }


    void 'create input datetime with value without attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget(value:value)

        when:
            def html = dateTimeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "datetime"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input dateTime without value with attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget(htmlAttrs:attrs)

        when:
            def html = dateTimeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "datetime"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "datetime"

        where:
            attrs = ['size':10, 'name': 'datetime']
    }


    void 'create input datetime with value and attribs'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = dateTimeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "datetime"
            result.BODY.INPUT.@value.text() == value
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "datetime"

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'datetime']
    }

    void 'Test assets'() {
        setup:
            def dateTimeInputWidget = new DateTimeInputWidget()

        when:
            def result = dateTimeInputWidget.getAssets()

        then:
            result != null
    }
}

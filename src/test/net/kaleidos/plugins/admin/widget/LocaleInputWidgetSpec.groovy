package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class LocaleInputWidgetSpec extends Specification {
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

    void 'create input text locale without value nor attribs'() {
        setup:
            def localeInputWidget = new LocaleInputWidget()

        when:
            def html = localeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@value.text() == ""
    }

    void 'create input text locale with value without attribs'() {
        setup:
            def localeInputWidget = new LocaleInputWidget(value:value)

        when:
            def html = localeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input text locale without value with attribs'() {
        setup:
            def localeInputWidget = new LocaleInputWidget(htmlAttrs:attrs)

        when:
            def html = localeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@value.text() == ""

        where:
            attrs = ['size':10, 'name': 'test']
    }

    void 'create input text locale with value and attribs'() {
        setup:
            def localeInputWidget = new LocaleInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = localeInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@size.text() == "10"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@value.text() == value

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }

    void 'Update value'() {
        setup:
            def localeInputWidget = new LocaleInputWidget()
            localeInputWidget.internalAttrs['domainObject'] = [:]
            localeInputWidget.internalAttrs['propertyName'] = "test"

        when:
            localeInputWidget.value = value?"$value":value
            localeInputWidget.updateValue()

        then:
            localeInputWidget.internalAttrs.domainObject.test == new Locale(value)

        where:
            value << ["en", "de_DE", "_GB", "en_US_WIN", "de__POSIX", "zh_CN_#Hans", "zh_TW_#Hant-x-java", "th_TH_TH_#u-nu-thai", "NOT A LOCALE"]
    }
}

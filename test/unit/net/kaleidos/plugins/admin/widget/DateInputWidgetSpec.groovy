package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

import admin.test.AdminDomainTest

@Mock([AdminDomainTest])
class DateInputWidgetSpec extends Specification {
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

    void 'create input text without value nor attribs'() {
        setup:
            def dateInputWidget = new DateInputWidget()

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "date"
    }


    void 'create input text with value without attribs'() {
        setup:
            def dateInputWidget = new DateInputWidget(value:value)

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "date"
            result.BODY.INPUT.@value.text() == value.encodeAsHTML()

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input text without value with attribs'() {
        setup:
            def dateInputWidget = new DateInputWidget(htmlAttrs:attrs)

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "date"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@size.text() == "10"

        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input text with value and attribs'() {
        setup:
            def dateInputWidget = new DateInputWidget(value:value, htmlAttrs:attrs)

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "date"
            result.BODY.INPUT.@value.text() == value.encodeAsHTML()
            result.BODY.INPUT.@size.text() == "10"

        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }

    void 'update value'(){
        setup:
            def adminDomainTest = new AdminDomainTest()
            def dateInputWidget = new DateInputWidget(value:value)
            dateInputWidget.internalAttrs['domainObject'] = adminDomainTest
            dateInputWidget.internalAttrs['propertyName'] = 'lastAccess'
        when:
            dateInputWidget.updateValue()
        then:
            adminDomainTest.lastAccess == Date.parse("MM/dd/yyyy", value)


        where:
            value = "01/01/2000"


    }
}

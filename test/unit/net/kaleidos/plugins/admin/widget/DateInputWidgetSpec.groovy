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
            result.BODY.INPUT.@type.text() == "text"
    }


    void 'create input text with value without attribs'() {
        setup:
            def dateInputWidget = new DateInputWidget(value:value)

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@value.text() == "01/01/2000"

        where:
            value = Date.parse("MM/dd/yyyy", "01/01/2000")
    }

    void 'create input text with value without attribs with special format'() {
        setup:
            def dateInputWidget = new DateInputWidget(value:value)
            dateInputWidget.internalAttrs["dateFormat"] = "yyyyMMdd"

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@value.text() == "20000101"

        where:
            value = Date.parse("MM/dd/yyyy", "01/01/2000")
    }

    void 'create input text without value with attribs'() {
        setup:
            def dateInputWidget = new DateInputWidget(htmlAttrs:attrs)

        when:
            def html = dateInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT.size() == 1
            result.BODY.INPUT.@type.text() == "text"
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
            result.BODY.INPUT.@type.text() == "text"
            result.BODY.INPUT.@value.text() == "01/01/2000"
            result.BODY.INPUT.@size.text() == "10"

        where:
            value = Date.parse("MM/dd/yyyy", "01/01/2000")
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

    void 'update value with special format'(){
        setup:
            def adminDomainTest = new AdminDomainTest()
            def dateInputWidget = new DateInputWidget(value:value)
            dateInputWidget.internalAttrs["dateFormat"] = "yyyyMMdd"
            dateInputWidget.internalAttrs['domainObject'] = adminDomainTest
            dateInputWidget.internalAttrs['propertyName'] = 'lastAccess'
        when:
            dateInputWidget.updateValue()
        then:
            adminDomainTest.lastAccess == Date.parse("yyyyMMdd", "20000101")


        where:
            value = "20000101"
    }

    void 'update value with special format (null)'(){
        setup:
            def adminDomainTest = new AdminDomainTest()
            def dateInputWidget = new DateInputWidget(value:value)
            dateInputWidget.internalAttrs["dateFormat"] = "yyyyMMdd"
            dateInputWidget.internalAttrs['domainObject'] = adminDomainTest
            dateInputWidget.internalAttrs['propertyName'] = 'lastAccess'
        when:
            dateInputWidget.updateValue()
        then:
            adminDomainTest.lastAccess == null
        where:
            value = null
    }

    void 'Get value for json'(){
        setup:
            def adminDomainTest = new AdminDomainTest()
            def dateInputWidget = new DateInputWidget(value:value)
            dateInputWidget.internalAttrs["dateFormat"] = "yyyyMMdd"
            dateInputWidget.internalAttrs['domainObject'] = adminDomainTest
            dateInputWidget.internalAttrs['propertyName'] = 'lastAccess'
        when:
            def result = dateInputWidget.getValueForJson()

        then:
            result == value

        where:
            value = "20000101"
    }


    void 'Get assets'(){
        setup:
            def adminDomainTest = new AdminDomainTest()
            def dateInputWidget = new DateInputWidget()

        when:
            def result = dateInputWidget.getAssets()

        then:
            result != null
    }

}

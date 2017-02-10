package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

import admin.test.AdminDomainTest

@Mock([AdminDomainTest])
class CheckboxInputWidgetSpec extends Specification {
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

    void 'create input checkbox without value nor attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget()

        when:
            def html = checkboxInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT != null
            result.BODY.INPUT.@type.text() == "checkbox"
    }

    @Unroll
    void 'create input checkbox with value and text:#text and without attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value:value, internalAttrs:['text':text])

        when:
            def html = checkboxInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT != null
            result.BODY.INPUT.@type.text() == "checkbox"
            result.BODY.INPUT.@checked.text() == "checked"

        where:
            value = "<script>alert(1234)</script>"
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create input checkbox without value and text:#text and without attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value:value, internalAttrs:['text':text])

        when:
            def html = checkboxInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT != null
            result.BODY.INPUT.@type.text() == "checkbox"

        where:
            value = null
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create input checkbox with value and text:#text and with attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value:value, internalAttrs:['text':text], htmlAttrs:attrs)

        when:
            def html = checkboxInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT != null
            result.BODY.INPUT.@type.text() == "checkbox"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@size.text() == "10"

        where:
            attrs = ['size':10, 'name': 'test']
            value = "<script>alert(1234)</script>"
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create input checkbox without value and text:#text and with attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value:value, internalAttrs:['text':text], htmlAttrs:attrs)

        when:
            def html = checkboxInputWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.INPUT != null
            result.BODY.INPUT.@type.text() == "checkbox"
            result.BODY.INPUT.@name.text() == "test"
            result.BODY.INPUT.@size.text() == "10"

        where:
            attrs = ['size':10, 'name': 'test']
            value = null
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    void 'update value'(){
        setup:
            def adminDomainTest = new AdminDomainTest()
            def dateInputWidget = new CheckboxInputWidget(value:value)
            dateInputWidget.internalAttrs['domainObject'] = adminDomainTest
            dateInputWidget.internalAttrs['propertyName'] = 'ok'
        when:
            dateInputWidget.updateValue()
        then:
            adminDomainTest.ok == result

        where:
            value << ["true", null]
            result << [true, false]


    }
}

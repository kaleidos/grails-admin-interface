package net.kaleidos.plugins.admin.widget

import admin.test.AdminDomainTest
import grails.plugin.spock.*
import grails.test.mixin.Mock
import org.grails.plugins.codecs.HTMLCodec
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@Mock([AdminDomainTest])
class LabelWidgetSpec extends Specification {
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

    void 'create label without value nor attribs'() {
        setup:
            def labelWidget = new LabelWidget()

        when:
            def html = labelWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.LABEL != null
    }

    @Unroll
    void 'create label with value and without attribs'() {
        setup:
            def labelWidget = new LabelWidget(value:value)

        when:
            def html = labelWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.LABEL != null
            result.BODY.LABEL.text() == ok

        where:
            value << ["el valor es 1234", null, "<script>alert(0)</script>"]
            ok << ["el valor es 1234", "", "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create label with value and with attribs'() {
        setup:
            def labelWidget = new LabelWidget(value:value, htmlAttrs:attrs)

        when:
            def html = labelWidget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.LABEL != null
            result.BODY.LABEL.@name.text() == "test"
            result.BODY.LABEL.@size.text() == "10"
            result.BODY.LABEL.text() == ok

        where:
            value << ["el valor es 1234", null, "<script>alert(0)</script>"]
            ok << ["el valor es 1234", "", "<script>alert(0)</script>"]
            attrs = ['size':10, 'name': 'test']
    }

    void 'update value'(){
        setup:
            def adminDomainTest = new AdminDomainTest(name:oldValue)
            def dateInputWidget = new LabelWidget(value:newValue)
            dateInputWidget.internalAttrs['domainObject'] = adminDomainTest
            dateInputWidget.internalAttrs['propertyName'] = 'name'
        when:
            dateInputWidget.updateValue()
        then:
            adminDomainTest.name == oldValue

        where:
            oldValue = '1234'
            newValue = '5678'


    }
}

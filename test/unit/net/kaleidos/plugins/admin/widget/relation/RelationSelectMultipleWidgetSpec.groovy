package net.kaleidos.plugins.admin.widget.relation

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import admin.test.TestDomain
import admin.test.TestDomainRelation
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass


@Mock([TestDomain, TestDomainRelation])
class RelationSelectMultipleWidgetSpec extends Specification {
    def td1
    def td2
    def td3

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


        td1 = new TestDomain(name:'name1', year:2000).save()
        td2 = new TestDomain(name:'name2', year:2001).save()
        td3 = new TestDomain(name:'name3', year:2002).save()

    }

    void 'create relation select multiple widget'() {
        setup:
            def widget = new RelationSelectMultipleWidget()
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class).clazz

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.size() == 1
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.OPTION.size() == 3
            result.BODY.SELECT.OPTION[0].text() == td1.name
            result.BODY.SELECT.OPTION[0].@value.text() == "${td1.id}"
            result.BODY.SELECT.OPTION[1].text() == td2.name
            result.BODY.SELECT.OPTION[1].@value.text() == "${td2.id}"
            result.BODY.SELECT.OPTION[2].text() == td3.name
            result.BODY.SELECT.OPTION[2].@value.text() == "${td3.id}"
    }


    void 'update value'(){
        setup:
            def object = new TestDomainRelation()
            def widget = new RelationSelectMultipleWidget(value:[td2.id, td3.id])
            widget.internalAttrs['relatedDomainClass'] = new DefaultGrailsDomainClass(TestDomain.class).clazz
            widget.internalAttrs['domainObject'] = object
            widget.internalAttrs['propertyName'] = 'testDomain'
        when:
            widget.updateValue()
        then:
            td2 in object.testDomain
            td3 in object.testDomain
    }







}

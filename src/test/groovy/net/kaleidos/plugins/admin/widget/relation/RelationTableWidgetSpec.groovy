package net.kaleidos.plugins.admin.widget.relation

import grails.plugin.spock.*
import grails.test.mixin.*
import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import admin.test.TestDomain
import admin.test.TestDomainRelation
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.config.DomainConfig

import org.codehaus.groovy.grails.web.mapping.LinkGenerator



@Mock([TestDomain, TestDomainRelation])
class RelationTableWidgetSpec extends Specification {
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


    void 'create relation select widget'() {
        setup:
            def widget = new RelationTableWidget()
            widget.metaClass._getSlug = { return "testdomainrelation" }
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class).clazz
            widget.internalAttrs.domainClass = TestDomainRelation.class
            widget.internalAttrs.propertyName = 'testDomain'
            widget.internalAttrs.grailsDomainClass =  new DefaultGrailsDomainClass(TestDomainRelation.class)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfig(_) >> { return new DomainConfig(TestDomain.class) }

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.TABLE.size() == 1
            result.BODY.DIV.TABLE.@"data-property-name".text() == 'testDomain'
            result.BODY.DIV.TABLE.children().size() == 0

            result.BODY.DIV.DIV.size() == 1
            result.BODY.DIV.DIV.A.size() == 2
    }

    void 'create relation select widget - no domain config'() {
        setup:
            def widget = new RelationTableWidget()
            widget.metaClass._getSlug = { return "testdomainrelation" }
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class).clazz
            widget.internalAttrs.domainClass = TestDomainRelation.class
            widget.internalAttrs.propertyName = 'testDomain'
            widget.internalAttrs.grailsDomainClass =  new DefaultGrailsDomainClass(TestDomainRelation.class)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfig(_) >> { return null }

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.TABLE.size() == 1
            result.BODY.DIV.TABLE.@"data-property-name".text() == 'testDomain'
            result.BODY.DIV.TABLE.children().size() == 0

            result.BODY.DIV.DIV.size() == 1
            result.BODY.DIV.DIV.A.size() == 2
            result.BODY.DIV.DIV.A[0].@disabled.text() == 'disabled'
            result.BODY.DIV.DIV.A[1].@disabled.text() == 'disabled'
    }


    void 'create relation select widget with value'() {
        setup:
            def object = new TestDomainRelation()
            object.addToTestDomain (td1)
            object.addToTestDomain (td2)
            def widget = new RelationTableWidget(value:[td1.id, td2.id])
            widget.metaClass._getSlug = { return "testdomainrelation" }
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class).clazz
            widget.internalAttrs.domainClass = TestDomainRelation.class
            widget.internalAttrs.propertyName = 'testDomain'
            widget.internalAttrs.domainObject = object
            widget.internalAttrs.grailsDomainClass =  new DefaultGrailsDomainClass(TestDomainRelation.class)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfig(_) >> { return new DomainConfig(TestDomain.class) }

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.TABLE.size() == 1
            result.BODY.DIV.TABLE.@"data-property-name".text() == 'testDomain'
            result.BODY.DIV.TABLE.TR.size() == 2
            result.BODY.DIV.TABLE.TR[0].TD.size() == 2
            result.BODY.DIV.TABLE.TR[0].TD[0].A.text() == td1.name

            result.BODY.DIV.TABLE.TR[1].TD.size() == 2
            result.BODY.DIV.TABLE.TR[1].TD[0].A.text() == td2.name

            result.BODY.DIV.DIV.size() == 1
            result.BODY.DIV.DIV.A.size() == 2
    }

    void 'create relation select widget with value - no domain config'() {
        setup:
            def object = new TestDomainRelation()
            object.addToTestDomain (td1)
            object.addToTestDomain (td2)
            def widget = new RelationTableWidget(value:[td1.id, td2.id])
            widget.metaClass._getSlug = { return "testdomainrelation" }
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class).clazz
            widget.internalAttrs.domainClass = TestDomainRelation.class
            widget.internalAttrs.propertyName = 'testDomain'
            widget.internalAttrs.domainObject = object
            widget.internalAttrs.grailsDomainClass =  new DefaultGrailsDomainClass(TestDomainRelation.class)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfig(_) >> { return null }

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.TABLE.size() == 1
            result.BODY.DIV.TABLE.@"data-property-name".text() == 'testDomain'
            result.BODY.DIV.TABLE.TR.size() == 2
            result.BODY.DIV.TABLE.TR[0].TD.size() == 1
            result.BODY.DIV.TABLE.TR[0].TD[0].LABEL.text() == td1.name

            result.BODY.DIV.TABLE.TR[1].TD.size() == 1
            result.BODY.DIV.TABLE.TR[1].TD[0].LABEL.text() == td2.name

            result.BODY.DIV.DIV.size() == 1
            result.BODY.DIV.DIV.A.size() == 2
            result.BODY.DIV.DIV.A[0].@disabled.text() == 'disabled'
            result.BODY.DIV.DIV.A[1].@disabled.text() == 'disabled'
    }


    void 'update value'(){
        setup:
            def object = new TestDomainRelation()
            def widget = new RelationTableWidget(value:[td2.id, td3.id])
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

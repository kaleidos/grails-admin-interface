package net.kaleidos.plugins.admin.widget.relation

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import admin.test.TestDomain
import admin.test.TestDomainRelationOne
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.config.DomainConfig

import org.codehaus.groovy.grails.web.mapping.LinkGenerator



@Mock([TestDomain, TestDomainRelationOne])
class RelationPopupOneWidgetSpec extends Specification {
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
            def widget = new RelationPopupOneWidget()
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfigForProperty(_,_) >> { return new DomainConfig(TestDomain.class) }
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class)
            widget.internalAttrs.domainClass = TestDomainRelationOne.class
            widget.internalAttrs.propertyName = 'testDomain'

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].A.size() == 1
            result.BODY.DIV.DIV[0].A.@name.text() == 'testDomain'
            result.BODY.DIV.DIV[0].A.text() == '<< empty >>'

            result.BODY.DIV.DIV[0].DIV.size() == 1
            result.BODY.DIV.DIV[0].DIV.A.size() == 3

            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == 'text'
            result.BODY.DIV.DIV[1].INPUT.@name.text() == 'testDomain'
            result.BODY.DIV.DIV[1].INPUT.@value.text() == ''
            result.BODY.DIV.DIV[1].INPUT.@class.text().contains('hidden')


    }


    void 'create relation select widget - no domain config'() {
        setup:
            def widget = new RelationPopupOneWidget()
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfigForProperty(_,_) >> { return null }
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class)
            widget.internalAttrs.domainClass = TestDomainRelationOne.class
            widget.internalAttrs.propertyName = 'testDomain'

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].LABEL.size() == 1
            result.BODY.DIV.DIV[0].LABEL.text() == '<< empty >>'

            result.BODY.DIV.DIV[0].DIV.size() == 1
            result.BODY.DIV.DIV[0].DIV.A.size() == 3
            result.BODY.DIV.DIV[0].DIV.A[0].@disabled.text() == 'disabled'
            result.BODY.DIV.DIV[0].DIV.A[1].@disabled.text() == 'disabled'
            result.BODY.DIV.DIV[0].DIV.A[2].@disabled.text() == 'disabled'

            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == 'text'
            result.BODY.DIV.DIV[1].INPUT.@name.text() == 'testDomain'
            result.BODY.DIV.DIV[1].INPUT.@value.text() == ''
            result.BODY.DIV.DIV[1].INPUT.@class.text().contains('hidden')


    }


    void 'create relation select widget with value'() {
        setup:
            def object = new TestDomainRelationOne()
            object.testDomain = td2
            def widget = new RelationPopupOneWidget(value:td2.id)
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfigForProperty(_,_) >> { return new DomainConfig(TestDomain.class) }
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class)
            widget.internalAttrs.domainClass = TestDomainRelationOne.class
            widget.internalAttrs.propertyName = 'testDomain'
            widget.internalAttrs['domainObject'] = object



        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].A.size() == 1
            result.BODY.DIV.DIV[0].A.@name.text() == 'testDomain'
            result.BODY.DIV.DIV[0].A.text() == td2.name
            result.BODY.DIV.DIV[0].DIV.size() == 1
            result.BODY.DIV.DIV[0].DIV.A.size() == 3

            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == 'text'
            result.BODY.DIV.DIV[1].INPUT.@name.text() == 'testDomain'
            result.BODY.DIV.DIV[1].INPUT.@value.text() == "${td2.id}"
            result.BODY.DIV.DIV[1].INPUT.@class.text().contains('hidden')
    }


    void 'create relation select widget with value - no domain config'() {
        setup:
            def object = new TestDomainRelationOne()
            object.testDomain = td2
            def widget = new RelationPopupOneWidget(value:td2.id)
            widget.grailsLinkGenerator = Mock(LinkGenerator)
            widget.adminConfigHolder = Mock(AdminConfigHolder)
            widget.adminConfigHolder.getDomainConfigForProperty(_,_) >> { return null }
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class)
            widget.internalAttrs.domainClass = TestDomainRelationOne.class
            widget.internalAttrs.propertyName = 'testDomain'
            widget.internalAttrs['domainObject'] = object



        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() == 1
            result.BODY.DIV.DIV.size() == 2
            result.BODY.DIV.DIV[0].LABEL.size() == 1
            result.BODY.DIV.DIV[0].LABEL.text() == td2.name
            result.BODY.DIV.DIV[0].DIV.size() == 1
            result.BODY.DIV.DIV[0].DIV.A.size() == 3
            result.BODY.DIV.DIV[0].DIV.A[0].@disabled.text() == 'disabled'
            result.BODY.DIV.DIV[0].DIV.A[1].@disabled.text() == 'disabled'
            result.BODY.DIV.DIV[0].DIV.A[2].@disabled.text() == 'disabled'

            result.BODY.DIV.DIV[1].INPUT.size() == 1
            result.BODY.DIV.DIV[1].INPUT.@type.text() == 'text'
            result.BODY.DIV.DIV[1].INPUT.@name.text() == 'testDomain'
            result.BODY.DIV.DIV[1].INPUT.@value.text() == "${td2.id}"
            result.BODY.DIV.DIV[1].INPUT.@class.text().contains('hidden')
    }


    void 'update value'(){
        setup:
            def object = new TestDomainRelationOne()
            def widget = new RelationPopupOneWidget(value:td2.id)
            widget.internalAttrs['relatedDomainClass'] = new DefaultGrailsDomainClass(TestDomain.class).clazz
            widget.internalAttrs['domainObject'] = object
            widget.internalAttrs['propertyName'] = 'testDomain'
        when:
            widget.updateValue()
        then:
            object.testDomain == td2
    }





}

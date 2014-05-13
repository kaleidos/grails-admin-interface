package net.kaleidos.plugins.admin.widget.relation

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import admin.test.TestDomain
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass


@Mock(TestDomain)
class RelationSelectWidgetSpec extends Specification {

    void setup() {
        Object.metaClass.encodeAsHTML = {
            def encoder = new HTMLCodec().getEncoder()
            return encoder.encode(delegate)
        }


        new TestDomain(name:'name1', year:2000).save()
        new TestDomain(name:'name2', year:2001).save()
        new TestDomain(name:'name3', year:2002).save()

    }

    void 'create relation select multiple widget'() {
        setup:
            def widget = new RelationSelectWidget()
            widget.internalAttrs.relatedDomainClass = new DefaultGrailsDomainClass(TestDomain.class)

        when:
            def html = widget.render()

        then:
            html == "<select><option value=\"\">--</option><option value=\"1\">name1</option><option value=\"2\">name2</option><option value=\"3\">name3</option></select>"
    }





}


package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import grails.util.Holders


class EnumWidgetSpec extends Specification {
    @Shared
    def slurper

    void setupSpec() {
        def parser = new org.cyberneko.html.parsers.SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        slurper = new XmlSlurper(parser)

        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            admin.test.TestEnumDomain.class,
        ] as Class[])
    }


    def "create enum widget"() {
        setup:
            def widget = new EnumWidget()
            widget.internalAttrs["domainClass"] = admin.test.TestEnumDomain
            widget.internalAttrs["propertyName"] = "type"
            widget.htmlAttrs["required"] = true

        when:
            def html = widget.render()
            def result =  slurper.parseText(html)

        then:
            result.BODY.SELECT.size() == 1
            result.BODY.SELECT.OPTION.size() == 3
    }
}


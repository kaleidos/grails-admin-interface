package net.kaleidos.plugins.admin.renderer

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import admin.test.AdminDomainTest
import spock.lang.*
import grails.converters.JSON
import grails.util.Holders


class GrailsAdminPluginHtmlRendererServiceIntegrationSpec extends Specification {
    def grailsAdminPluginHtmlRendererService
    def adminConfigHolder
    def slurper

    def setup() {
        Holders.config = new ConfigObject()
        Holders.config.grails.plugin.admin.domains = [ "admin.test.AdminDomainTest" ]

        adminConfigHolder.initialize()

        def parser = new org.cyberneko.html.parsers.SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        slurper = new XmlSlurper(parser)
    }

    def cleanup() {
    }

    void "test renderEditForm"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            def html = grailsAdminPluginHtmlRendererService.renderEditFormFields(adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() ==  13
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test renderCreateForm"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginHtmlRendererService.renderCreateFormFields("admin.test.AdminDomainTest")
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() ==  13
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test render list line"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginHtmlRendererService.renderListLine(adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TD.size() == 13
    }

    void "test render list line injection"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            adminDomainTest.name = "<td>0</td>"
            def html = grailsAdminPluginHtmlRendererService.renderListLine(adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TD.size() == 13
    }

    void "test render list titles"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginHtmlRendererService.renderListTitle("admin.test.AdminDomainTest", 'name', 'asc')
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TH.size() == 13
    }
}

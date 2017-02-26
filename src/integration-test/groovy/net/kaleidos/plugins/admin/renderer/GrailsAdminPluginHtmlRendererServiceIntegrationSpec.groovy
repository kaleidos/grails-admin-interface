package net.kaleidos.plugins.admin.renderer

import admin.test.AdminDomainTest
import grails.util.Holders
import spock.lang.Specification


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
            result.BODY.DIV.size() ==  14
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
            result.BODY.DIV.size() ==  14
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test renderCreateForm with normal groups"() {
        setup:
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [ "admin.test.AdminDomainTest" ]
            Holders.config.grails.plugin.admin.domain.AdminDomainTest = {
                groups {
                    "Group 1" fields: ['name','surname','age','email']
                    "Group 2" fields: ['web','longNumber','year']
                }
            }
            adminConfigHolder.initialize()

            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginHtmlRendererService.renderCreateFormFields("admin.test.AdminDomainTest")
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() ==  9
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test renderCreateForm with collapsible groups"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [ "admin.test.AdminDomainTest" ]
            Holders.config.grails.plugin.admin.domain.AdminDomainTest = {
                groups {
                    "Group 1" style:"collapse", fields: ['name','surname','age','email']
                    "Group 2" style:"collapse", fields: ['web','longNumber','year']
                }
            }
            adminConfigHolder.initialize()

        when:
            def html = grailsAdminPluginHtmlRendererService.renderCreateFormFields("admin.test.AdminDomainTest")
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() ==  9
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test render list line"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginHtmlRendererService.renderListLine("admin.test.AdminDomainTest", adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TD.size() == 14
    }

    void "test render list line injection"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            adminDomainTest.name = "<td>0</td>"
            def html = grailsAdminPluginHtmlRendererService.renderListLine("admin.test.AdminDomainTest", adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TD.size() == 14
    }

    void "test render list titles"() {
        setup:
            def adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginHtmlRendererService.renderListTitle("admin.test.AdminDomainTest", 'name', 'asc')
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TH.size() == 14
    }
}

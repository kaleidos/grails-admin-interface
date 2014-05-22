package net.kaleidos.plugins.admin.builder

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import admin.test.AdminDomainTest
import spock.lang.*
import grails.converters.JSON


class GrailsAdminPluginBuilderServiceIntegrationSpec extends Specification {
    def grailsAdminPluginBuilderService
    def adminDomainTest
    def grailsApplication

    def slurper

    def setup() {
        grailsApplication.configureLoadedClasses([
            admin.test.AdminDomainTest.class
        ] as Class[])

        def testDomains = [ "admin.test.AdminDomainTest" ]

        def config = new ConfigObject()
        config.grails.plugin.admin.domains = testDomains
        config.grails.plugin.admin.access_root = "myadmin"
        config.grails.plugin.admin.role = "ROLE_SUPER"

        def configHolder = new AdminConfigHolder(config)
        configHolder.grailsApplication = grailsApplication

        grailsAdminPluginBuilderService.adminConfigHolder = configHolder


        configHolder.initialize()

        def parser = new org.cyberneko.html.parsers.SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        slurper = new XmlSlurper(parser)
    }

    def cleanup() {
    }

    void "test renderEditForm"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            def html = grailsAdminPluginBuilderService.renderEditFormFields(adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() ==  12
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test renderCreateForm"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginBuilderService.renderCreateFormFields("admin.test.AdminDomainTest")
            def result = slurper.parseText(html)

        then:
            result.BODY.DIV.size() ==  12
            result.BODY.DIV.each { div ->
                div.LABEL.@for.text() !=  null
            }
    }

    void "test render list line"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginBuilderService.renderListLine(adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TD.size() == 12
    }

    void "test render list line injection"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            adminDomainTest.name = "<td>0</td>"
            def html = grailsAdminPluginBuilderService.renderListLine(adminDomainTest)
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TD.size() == 12
    }

    void "test render list titles"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        when:
            def html = grailsAdminPluginBuilderService.renderListTitle("admin.test.AdminDomainTest", 'name', 'asc')
            def result = slurper.parseText(html)

        then:
            result.BODY.TABLE.TR.TH.size() == 12
    }

    void 'render single object as json'() {
        given: 'an object'
            adminDomainTest = new AdminDomainTest(id:1, name:'a')

        when: 'ask to render as json'
            def json = grailsAdminPluginBuilderService.renderObjectAsJson(adminDomainTest)
            def result = JSON.parse(json)

        then: 'should return a json response'
            result.size() == 14
            result['id'] != null
            result['__text__'] != null
    }

    void 'render list as json'() {
        given: 'an list object'
            def list = [
                    new AdminDomainTest(id:1, name:'Paul1'),
                    new AdminDomainTest(id:2, name:'Paul2'),
                    new AdminDomainTest(id:3, name:'Paul3'),
                    new AdminDomainTest(id:4, name:'Paul4')
                ]

        when: 'ask to render list as json'
            def json = grailsAdminPluginBuilderService.renderListAsJson(list)
            def result = JSON.parse(json)

        then: 'should return a json response'
            result.size() == 4
            result.each {
                it.size() == 14
            }
    }
}

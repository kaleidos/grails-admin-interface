package net.kaleidos.plugins.admin.builder

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import admin.test.AdminDomainTest
import spock.lang.*


class GrailsAdminPluginBuilderServiceIntegrationSpec extends Specification {
    def grailsAdminPluginBuilderService
    def adminDomainTest
    def grailsApplication

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

    }

    def cleanup() {
    }

    void "test renderEditForm"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            def html = grailsAdminPluginBuilderService.renderEditFormFields(adminDomainTest)
        then:
            html == "<div class=\"form-group\"><label for=\"age\">Age</label><input type=\"number\" value=\"25\" name=\"age\" min=\"18\" max=\"100\" required=\"true\" /></div><div class=\"form-group\"><label for=\"birthday\">Birthday</label><input type=\"date\" name=\"birthday\" required=\"true\" /></div><div class=\"form-group\"><label for=\"country\">Country</label><select name=\"country\"><option value=\"\">--</option><option value=\"Canada\">Canada</option><option value=\"Spain\">Spain</option><option value=\"USA\">USA</option></select></div><div class=\"form-group\"><label for=\"email\">Email</label><input type=\"email\" value=\"paul@example.com\" name=\"email\" /></div><div class=\"form-group\"><label for=\"longNumber\">LongNumber</label><input type=\"number\" name=\"longNumber\" required=\"true\" /></div><div class=\"form-group\"><label for=\"name\">Name</label><input type=\"text\" value=\"Paul\" name=\"name\" /></div><div class=\"form-group\"><label for=\"surname\">Surname</label><input type=\"text\" name=\"surname\" maxlength=\"100\" /></div><div class=\"form-group\"><label for=\"web\">Web</label><input type=\"url\" name=\"web\" /></div><div class=\"form-group\"><label for=\"year\">Year</label><input type=\"number\" name=\"year\" max=\"2020\" min=\"2014\" /></div>"
    }

    void "test renderCreateForm"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            def html = grailsAdminPluginBuilderService.renderCreateFormFields("admin.test.AdminDomainTest")
        then:
            html == "<div class=\"form-group\"><label for=\"age\">Age</label><input type=\"number\" name=\"age\" min=\"18\" max=\"100\" required=\"true\" /></div><div class=\"form-group\"><label for=\"birthday\">Birthday</label><input type=\"date\" name=\"birthday\" required=\"true\" /></div><div class=\"form-group\"><label for=\"country\">Country</label><select name=\"country\"><option value=\"\">--</option><option value=\"Canada\">Canada</option><option value=\"Spain\">Spain</option><option value=\"USA\">USA</option></select></div><div class=\"form-group\"><label for=\"email\">Email</label><input type=\"email\" name=\"email\" /></div><div class=\"form-group\"><label for=\"longNumber\">LongNumber</label><input type=\"number\" name=\"longNumber\" required=\"true\" /></div><div class=\"form-group\"><label for=\"name\">Name</label><input type=\"text\" name=\"name\" /></div><div class=\"form-group\"><label for=\"surname\">Surname</label><input type=\"text\" name=\"surname\" maxlength=\"100\" /></div><div class=\"form-group\"><label for=\"web\">Web</label><input type=\"url\" name=\"web\" /></div><div class=\"form-group\"><label for=\"year\">Year</label><input type=\"number\" name=\"year\" max=\"2020\" min=\"2014\" /></div>"
    }


    void "test render list line"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            def html = grailsAdminPluginBuilderService.renderListLine(adminDomainTest)
        then:
            html == "<td>25</td><td>&nbsp;</td><td>&nbsp;</td><td>paul@example.com</td><td>&nbsp;</td><td>Paul</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
    }

    void "test render list line injection"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            adminDomainTest.name = "<alert>0</alert>"
            def html = grailsAdminPluginBuilderService.renderListLine(adminDomainTest)
        then:
            html == "<td>25</td><td>&nbsp;</td><td>&nbsp;</td><td>paul@example.com</td><td>&nbsp;</td><td>&lt;alert&gt;0&lt;/alert&gt;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
    }

    void "test render list titles"() {
        setup:
            adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')
        when:
            def html = grailsAdminPluginBuilderService.renderListTitle("admin.test.AdminDomainTest")

        then:
            html == "<th>age</th><th>birthday</th><th>country</th><th>email</th><th>longNumber</th><th>name</th><th>surname</th><th>web</th><th>year</th>"
    }

    void 'render single object as json'() {
        given: 'an object'
            adminDomainTest = new AdminDomainTest(id:1, name:'a')

        when: 'ask to render as json'
            def json = grailsAdminPluginBuilderService.renderObjectAsJson(adminDomainTest)

        then: 'should return a json response'
            json == "{\"id\":1,\"name\":\"a\"}"
    }

    void 'render object as json'() {
        given: 'an object'
            adminDomainTest = new AdminDomainTest(id:1,name:'Paul', age:25, email:'p@ex.com')

        when: 'ask to render as json'
            def json = grailsAdminPluginBuilderService.renderObjectAsJson(adminDomainTest)

        then: 'should return a json response'
            json == "{\"id\":1,\"age\":25,\"email\":\"p@ex.com\",\"name\":\"Paul\"}"
    }

    void 'render object with date as json'() {
        given: 'an object'
            def dateValue = new Date()
            adminDomainTest = new AdminDomainTest(id:1,name:'Paul', age:25, email:'p@ex.com',birthday:dateValue)

        when: 'ask to render as json'
            def json = grailsAdminPluginBuilderService.renderObjectAsJson(adminDomainTest)

        then: 'should return a json response'
            json == "{\"id\":1,\"age\":25,\"birthday\":\"${dateValue.toString()}\",\"email\":\"p@ex.com\",\"name\":\"Paul\"}"
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

        then: 'should return a json response'
            json == "[{\"id\":1,\"name\":\"Paul1\"},{\"id\":2,\"name\":\"Paul2\"},{\"id\":3,\"name\":\"Paul3\"},{\"id\":4,\"name\":\"Paul4\"}]"
    }
}

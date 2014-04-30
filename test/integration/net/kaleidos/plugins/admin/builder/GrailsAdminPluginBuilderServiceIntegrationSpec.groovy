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
        adminDomainTest = new AdminDomainTest(name:'Paul', age:25, email:'paul@example.com')

        grailsApplication.configureLoadedClasses([
            admin.test.AdminDomainTest.class
        ] as Class[])

        def testDomains = {
            "admin.test.AdminDomainTest"()
        }

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
        when:
            def html = grailsAdminPluginBuilderService.renderEditForm(adminDomainTest)
        then:
            html == "<form><div class=\"form-group\"><label for=\"age\">Age</label><input type=\"number\" value=\"25\" name=\"age\" min=\"18\" max=\"100\" required=\"true\" /><div><div class=\"form-group\"><label for=\"country\">Country</label><select name=\"country\"><option value=\"\">--</option><option value=\"Canada\">Canada</option><option value=\"Spain\">Spain</option><option value=\"USA\">USA</option></select><div><div class=\"form-group\"><label for=\"email\">Email</label><input type=\"email\" value=\"paul@example.com\" name=\"email\" /><div><div class=\"form-group\"><label for=\"id\">Id</label><input type=\"number\" name=\"id\" /><div><div class=\"form-group\"><label for=\"longNumber\">LongNumber</label><input type=\"number\" name=\"longNumber\" required=\"true\" /><div><div class=\"form-group\"><label for=\"name\">Name</label><input type=\"text\" value=\"Paul\" name=\"name\" /><div><div class=\"form-group\"><label for=\"surname\">Surname</label><input type=\"text\" name=\"surname\" maxlength=\"100\" /><div><div class=\"form-group\"><label for=\"version\">Version</label><input type=\"number\" name=\"version\" /><div><div class=\"form-group\"><label for=\"web\">Web</label><input type=\"url\" name=\"web\" /><div><div class=\"form-group\"><label for=\"year\">Year</label><input type=\"number\" name=\"year\" max=\"2020\" min=\"2014\" /><div></form>"
    }


    void "test render list line"() {
        when:
            def html = grailsAdminPluginBuilderService.renderListLine(adminDomainTest)
        then:
            html == "<td>25</td><td>&nbsp;</td><td>paul@example.com</td><td>&nbsp;</td><td>&nbsp;</td><td>Paul</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
    }

    void "test render list line injection"() {
        when:
            adminDomainTest.name = "<alert>0</alert>"
            def html = grailsAdminPluginBuilderService.renderListLine(adminDomainTest)
        then:
            html == "<td>25</td><td>&nbsp;</td><td>paul@example.com</td><td>&nbsp;</td><td>&nbsp;</td><td>&lt;alert&gt;0&lt;/alert&gt;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>"
    }
}

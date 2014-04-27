package net.kaleidos.plugins.admin.config

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

class AdminConfigHolderSpec extends Specification {
    def grailsApplication

    void setup() {
        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.configureLoadedClasses([admin.test.TestDomain.class] as Class[])
    }

    void "Obtain configuration"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains
            config.grails.plugin.admin.access_root = testAccessRoot
            config.grails.plugin.admin.role = testRole

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        when:
            configHolder.validateConfiguration()

        then:
            notThrown(RuntimeException)
            configHolder.domains != null
            configHolder.domains.size() == 1
            configHolder.accessRoot == testAccessRoot
            configHolder.role == testRole

        where:
            testDomains = [ "admin.test.TestDomain" ]
            testAccessRoot = "myadmin"
            testRole = "ROLE_SUPER"
    }

    void "Default configuration"(){
        setup: "configuration"
            def config = new ConfigObject()

        when:
            def configHolder = new AdminConfigHolder(config)

        then:
            configHolder.domains != null
            configHolder.accessRoot != null
            configHolder.role != null
    }

    void "Domain class doesn't exist"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        when:
            configHolder.validateConfiguration()

        then:
            thrown(RuntimeException)

        where:
            testDomains = [ "NOT.EXISTS" ]
    }
}

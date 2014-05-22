package net.kaleidos.plugins.admin.config

import spock.lang.Specification
import spock.lang.IgnoreRest

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingsHolder
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingEvaluator
import org.springframework.web.context.WebApplicationContext

import grails.util.Holders

import admin.test.*

class AdminConfigHolderSpec extends Specification {
    void setup() {
        def grailsApplication = new DefaultGrailsApplication()
        grailsApplication.configureLoadedClasses([
            admin.test.TestDomain.class,
            admin.test.TestOtherDomain.class,
            admin.test.TestExtendsDomain.class
        ] as Class[])
        Holders.grailsApplication = grailsApplication
    }

    void "Obtain domain configuration"(){
        setup: "configuration"
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = testDomains

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()

        then:
            configHolder.domains != null
            configHolder.domains.size() == 2

        where:
            testDomains = [
                "admin.test.TestDomain",
                "admin.test.TestOtherDomain"
            ]
    }

    void "Obtain configuration (Domain closure configuration)"(){
        setup: "configuration"
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [
                "admin.test.TestDomain"
            ]
            Holders.config.grails.plugin.admin.domain.TestDomain = {
                list excludes: ['name', 'year']
            }

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()

        then:
            configHolder.domains != null
            configHolder.domains.size() == 1
            configHolder.domains["admin.test.TestDomain"] != null
            configHolder.domains["admin.test.TestDomain"].getExcludes('list') != null
            configHolder.domains["admin.test.TestDomain"].getExcludes('list').size() == 2

    }

    void "Obtain configuration (Domain admin file)"(){
        setup: "configuration"
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [ "admin.test.TestDomain" ]
            Holders.config.grails.plugin.admin.domain.TestDomain = "admin.test.TestDomainAdmin"

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()

        then:
            configHolder.domains != null
            configHolder.domains.size() == 1
            configHolder.domains["admin.test.TestDomain"] != null
            configHolder.domains["admin.test.TestDomain"].getExcludes('list') != null
            configHolder.domains["admin.test.TestDomain"].getExcludes('list').size() == 2
    }

    void "Default configuration"(){
        setup: "configuration"
            Holders.config = new ConfigObject()

        when:
            def configHolder = new AdminConfigHolder()

        then:
            configHolder.domains != null
            configHolder.domains.size() == 0
    }

    void "Domain class doesn't exist"(){
        setup: "configuration"
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [ "NoExistsDomain" ]

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()

        then:
            thrown(RuntimeException)
    }

    def "Get all domain names"() {
        setup:
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [
                "admin.test.TestDomain",
                "admin.test.TestOtherDomain"
            ]

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()
            def domainNames = configHolder.getDomainNames()

        then:
            domainNames == ["TestDomain", "TestOtherDomain"]
    }

    def "Get all lower domain names"() {
        setup:
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = [
                "admin.test.TestDomain",
                "admin.test.TestOtherDomain"
            ]

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()
            def domainNames = configHolder.slugDomainNames

        then:
            domainNames == ["testdomain", "testotherdomain"]
    }

    def "Given a slug, recover the DomainConfig object"() {
        setup:
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = testDomains

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()
            DomainConfig testDomainConfig = configHolder.getDomainConfigBySlug(testDomainSlug)

        then:
            testDomainConfig == configHolder.domains[testDomainClassName]

        where:
            testDomains = [ "admin.test.TestDomain" ]
            testDomainSlug = "testdomain"
            testDomainClassName = "admin.test.TestDomain"
    }

    def "Given a non existant slug, don't recover any DomainConfig object"() {
        setup:
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = testDomains

        and: "Config holder"
            def configHolder = new AdminConfigHolder()

        when:
            configHolder.initialize()
            DomainConfig testDomainConfig = configHolder.getDomainConfigBySlug(testDomainSlug)

        then:
            testDomainConfig == null

        where:
            testDomains = [ "admin.test.TestDomain" ]
            testDomainSlug = "testdomainBadSlug"
            testDomainClassName = "admin.test.TestDomain"
    }

    def "Get the domain config of a super class"() {
        setup:
            Holders.config = new ConfigObject()
            Holders.config.grails.plugin.admin.domains = testDomains

        and: "Config holder"
            def configHolder = new AdminConfigHolder()
            def obj =  new TestExtendsDomain()

        when:
            configHolder.initialize()
            DomainConfig testDomainConfig = configHolder.getDomainConfig(obj)

        then:
            testDomainConfig != null

        where:
            testDomains = [ "admin.test.TestDomain" ]
    }
}

package net.kaleidos.plugins.admin.config

import spock.lang.Specification
import spock.lang.IgnoreRest

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingsHolder
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingEvaluator
import org.springframework.web.context.WebApplicationContext

import admin.test.*

class AdminConfigHolderSpec extends Specification {
    def grailsApplication

    static GrailsAdminUrlMappings = Class.forName("GrailsAdminUrlMappings")

    void setup() {
        grailsApplication = new DefaultGrailsApplication()
        grailsApplication.configureLoadedClasses([
            admin.test.TestDomain.class,
            admin.test.TestOtherDomain.class,
            admin.test.TestExtendsDomain.class,
            GrailsAdminUrlMappings
        ] as Class[])
    }

    void "Obtain configuration (Access root)"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.access_root = testAccessRoot

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()

        then:
            configHolder.accessRoot == testAccessRoot

        where:
            testAccessRoot = "myadmin"
    }

    void "Obtain configuration (Role)"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.role = testRole

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()

        then:
            configHolder.role == testRole

        where:
            testRole = "ROLE_SUPER"
    }

    void "Obtain configuration (Domain list)"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

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

    @IgnoreRest
    void "Obtain configuration (Domain closure configuration)"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = [ "admin.test.TestDomain" ]
            config.grails.plugin.admin.domain.TestDomain = {
                list excludes: ['name', 'year']
            }

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()

        then:
            configHolder.domains != null
            configHolder.domains.size() == 1
            configHolder.domains["admin.test.TestDomain"] != null
            configHolder.domains["admin.test.TestDomain"].getExcludes('list') != null
            configHolder.domains["admin.test.TestDomain"].getExcludes('list').size() == 2

    }

    @IgnoreRest
    void "Obtain configuration (Domain admin file)"(){
        setup: "configuration"
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = [ "admin.test.TestDomain" ]
            config.grails.plugin.admin.domain.TestDomain = "admin.test.TestDomainAdmin"

        and: "Config holder"
            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

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
            def config = new ConfigObject()

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

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

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()

        then:
            thrown(RuntimeException)

        where:
            testDomains = [ "NoExistsDomain" ]
    }

    void "Test changes in the URL mappings"() {
        setup:
            def configHolder = new AdminConfigHolder()
            configHolder.grailsApplication = grailsApplication

        and: "New url mappings holder"
            def evaluator = new DefaultUrlMappingEvaluator((WebApplicationContext)null);
            def mappingList = evaluator.evaluateMappings(GrailsAdminUrlMappings.getDynamicUrlMapping(GrailsAdminUrlMappings.INTERNAL_URI))
            def urlMappingsHolder = new DefaultUrlMappingsHolder(mappingList)

        and: "Spring configuration"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()

        then:
            urlMappingsHolder.match("/admin") != null
            urlMappingsHolder.match("/admin/api/testDomain") != null
            urlMappingsHolder.match("/admin/api/testDomain/1") != null
            urlMappingsHolder.match("/admin/web/testDomain") != null
            urlMappingsHolder.match("/admin/web/testDomain/1") != null
    }

    def "Get all domain names"() {
        setup:
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()
            def domainNames = configHolder.getDomainNames()

        then:
            domainNames == ["TestDomain", "TestOtherDomain"]

        where:
            testDomains = [
                "admin.test.TestDomain",
                "admin.test.TestOtherDomain"
            ]
    }

    def "Get all lower domain names"() {
        setup:
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        when:
            configHolder.initialize()
            def domainNames = configHolder.slugDomainNames

        then:
            domainNames == ["testdomain", "testotherdomain"]

        where:
            testDomains = [
                "admin.test.TestDomain",
                "admin.test.TestOtherDomain"
            ]
    }

    def "Given a slug, recover the DomainConfig object"() {
        setup:
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

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
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

        and: "Url config"
            grailsApplication.mainContext = Mock(WebApplicationContext)
            def urlMappingsHolder = new DefaultUrlMappingsHolder([])
            grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

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
            def config = new ConfigObject()
            config.grails.plugin.admin.domains = testDomains

            def configHolder = new AdminConfigHolder(config)
            configHolder.grailsApplication = grailsApplication

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

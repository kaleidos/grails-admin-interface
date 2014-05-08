package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingsHolder
import org.springframework.context.ApplicationContext

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import spock.lang.Specification
import spock.lang.Unroll
import spock.lang.Shared

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.builder.GrailsAdminPluginBuilderService

import admin.test.TestDomain

@TestFor(GrailsAdminPluginApiController)
class GrailsAdminPluginApiControllerSpec extends Specification {
    @Shared
    def adminConfigHolder

    def setupSpec() {
        def grailsApplication = new DefaultGrailsApplication()
        grailsApplication.configureLoadedClasses([
            admin.test.TestDomain.class,
        ] as Class[])

        grailsApplication.mainContext = Mock(ApplicationContext)
        def urlMappingsHolder = new DefaultUrlMappingsHolder([])
        grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER") >> urlMappingsHolder

        def config = new ConfigObject()
        config.grails.plugin.admin.domains = [ "admin.test.TestDomain" ]

        adminConfigHolder = new AdminConfigHolder(config)
        adminConfigHolder.grailsApplication = grailsApplication
        adminConfigHolder.initialize()
    }

    def setup() {
        controller.adminConfigHolder = adminConfigHolder
    }

    void 'Retrieve domain list'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.grailsAdminPluginGenericService.listDomain(TestDomain.class) >> [
                new TestDomain(name:"test1", year:2000),
                new TestDomain(name:"test2", year:2001),
                new TestDomain(name:"test3", year:2002),
            ]
            
            controller.grailsAdminPluginBuilderService = Mock(GrailsAdminPluginBuilderService)
            controller.grailsAdminPluginBuilderService.renderListAsJson(_) >> "[{\"name\":\"test1\"}, {\"name\":\"test2\"}, {\"name\":\"test3\"}]"

        when:
            controller.getAdminAction(domain, null)
            def result = response.json
            
        then:
            response.status == 200
            result.size() == 3

        where:
            domain = 'testdomain'
    }

    void 'Retrieve domain detail'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.grailsAdminPluginGenericService.retrieveDomain(TestDomain.class, 1) >> new TestDomain(name:name, year:year)

            controller.grailsAdminPluginBuilderService = Mock(GrailsAdminPluginBuilderService)
            controller.grailsAdminPluginBuilderService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${year}\"}"}

        when:
            controller.getAdminAction(domain, 1)
            def result = response.json
            println result
        then:
            response.status == 200
            result.name == name
            result.year == year

        where:
            name = "test"
            year = "2000"
            domain = 'testdomain'
    }

    void 'Save domain'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.request.contentType = "application/json"
            controller.request.content = '{"name":"TEST", year: 2000}'

        when:
            controller.putAdminAction(domain)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginGenericService.saveDomain(TestDomain.class, ["name":name, "year":year]) >> new TestDomain(name:name, year:year)

            response.status == 200
            result.name == name
            result.year == year

        where:
            name = "TEST"
            year = 2000
            domain = 'testdomain'
    }

    void 'Update domain'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.request.contentType = "application/json"
            controller.request.content = '{year: 2003}'

        when:
            controller.postAdminAction(domain,1)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginGenericService.updateDomain(TestDomain.class, 1, ["year":year]) >> new TestDomain(name:name, year:year)

            response.status == 200
            result.name == name
            result.year == year

        where:
            name = "TEST"
            year = 2003
            domain = 'testdomain'
    }

    void 'Delete domain'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)

        when:
            controller.deleteAdminAction(domain,1)

        then:
            1 * controller.grailsAdminPluginGenericService.deleteDomain(TestDomain.class, 1)
            response.status == 204

        where:
            domain = 'testdomain'
    }
}

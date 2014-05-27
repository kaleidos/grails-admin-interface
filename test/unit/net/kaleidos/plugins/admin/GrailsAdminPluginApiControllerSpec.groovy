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
import net.kaleidos.plugins.admin.renderer.GrailsAdminPluginJsonRendererService

import grails.util.Holders
import grails.validation.ValidationException

import admin.test.TestDomain

@TestFor(GrailsAdminPluginApiController)
class GrailsAdminPluginApiControllerSpec extends Specification {
    @Shared
    def adminConfigHolder

    def setupSpec() {
        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            admin.test.TestDomain.class,
        ] as Class[])

        Holders.config = new ConfigObject()
        Holders.config.grails.plugin.admin.domains = [ "admin.test.TestDomain" ]

        adminConfigHolder = new AdminConfigHolder()
        adminConfigHolder.initialize()
    }

    def setup() {
        controller.adminConfigHolder = adminConfigHolder
    }


    void 'Dashboard API'() {
        when:
            controller.listDomains()
            def result = response.json

        then:
            result.size() == 1
            result.contains "admin.test.TestDomain"

    }

    void 'Retrieve domain detail'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.grailsAdminPluginGenericService.retrieveDomain(TestDomain.class, 1) >> new TestDomain(name:name, year:year)

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${year}\"}"}

        when:
            controller.getAdminAction(domain, 1)
            def result = response.json

        then:
            response.status == 200
            result.name == name
            result.year == year

        where:
            name = "test"
            year = "2000"
            domain = 'testdomain'
    }

    void 'Retrieve domain detail (not found)'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.grailsAdminPluginGenericService.retrieveDomain(TestDomain.class, 1) >> null

        when:
            controller.getAdminAction(domain, 1)
            def result = response.json

        then:
            response.status == 404
            result.error != null

        where:
            domain = 'testdomain'
    }

    void 'Retrieve domain list'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.grailsAdminPluginGenericService.listDomain(TestDomain.class) >> [
                new TestDomain(name:"test1", year:2000),
                new TestDomain(name:"test2", year:2001),
                new TestDomain(name:"test3", year:2002),
            ]

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderListAsJson(_) >> "[{\"name\":\"test1\"}, {\"name\":\"test2\"}, {\"name\":\"test3\"}]"

        when:
            controller.getAdminAction(domain, null)
            def result = response.json

        then:
            response.status == 200
            result.size() == 3

        where:
            domain = 'testdomain'
    }

    void 'Save domain'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.request.contentType = "application/json"
            controller.request.content = '{"name":"TEST", year: 2000}'

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${yearToString}\"}"}

        when:
            controller.putAdminAction(domain)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginGenericService.saveDomain(TestDomain.class, ["name":name, "year":year]) >> new TestDomain(name:name, year:year)

            response.status == 200
            result.name == name
            result.year == yearToString

        where:
            name = "TEST"
            year = 2000
            yearToString = year.toString()
            domain = 'testdomain'
    }

    void 'Save domain (not valid)'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.request.contentType = "application/json"
            controller.request.content = '{"name":"TEST", year: 2000}'

            mockForConstraintsTests(TestDomain)
            def domainInstance = new TestDomain()
            domainInstance.validate()
            def validationErrors = domainInstance.errors

        when:
            controller.putAdminAction(domain)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginGenericService.saveDomain(TestDomain.class, ["name":name, "year":year]) >> { throw new ValidationException('Validation', validationErrors)}

            response.status == 500
            result.errors != null

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

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${yearToString}\"}"}

        when:
            controller.postAdminAction(domain,1)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginGenericService.updateDomain(TestDomain.class, 1, ["year":year]) >> new TestDomain(name:name, year:year)

            response.status == 200
            result.name == name
            result.year == yearToString

        where:
            name = "TEST"
            year = 2003
            yearToString = year.toString()
            domain = 'testdomain'
    }

    void 'Update domain (saving failed)'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
            controller.request.contentType = "application/json"
            controller.request.content = '{year: 2003}'

        when:
            controller.postAdminAction(domain,1)

        then:
            1 * controller.grailsAdminPluginGenericService.updateDomain(TestDomain.class, 1, ["year":year]) >> { throw new RuntimeException() }

            response.status == 500
            response.json.error != null

        where:
            name = "TEST"
            year = 2003
            yearToString = year.toString()
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

    void 'Delete domain (runtime exception)'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)

        when:
            controller.deleteAdminAction(domain,1)

        then:
            1 * controller.grailsAdminPluginGenericService.deleteDomain(TestDomain.class, 1) >> { throw new RuntimeException() }
            response.status == 500
            response.json.error != null

        where:
            domain = 'testdomain'
    }

    void 'No configured domain (slug)'() {
        when:
            controller.putAdminAction(slug)
            def result = response.json

        then:
            response.status == 404
            result.error != null

        where:
            slug = 'nofound'
    }

    void 'No configured domain (slug+id)'() {
        when:
            controller."$method"(slug, null)
            def result = response.json

        then:
            response.status == 404
            result.error != null

        where:
            slug = 'nofound'
            method << ['getAdminAction', 'postAdminAction', 'deleteAdminAction']
    }

    void 'No configured domain (Relation)'() {
        when:
            controller."$method"(slug, null, property, null)
            def result = response.json

        then:
            response.status == 404
            result.error != null

        where:
            slug = 'nofound'
            property = 'testproperty'
            method << ['deleteRelatedAdminAction', 'putRelatedAdminAction']
    }

    void 'Test add relation method'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)

        when:
            controller.putRelatedAdminAction(domain, 1, 'domain', 1)

        then:
            1 * controller.grailsAdminPluginGenericService.putRelatedDomain(TestDomain.class, 1, 'domain', 1) >> {}
            response.status == 204

        where:
            domain = 'testdomain'
    }

    void 'Test add relation method (error saving)'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)

        when:
            controller.putRelatedAdminAction(domain, 1, 'domain', 1)

        then:
            1 * controller.grailsAdminPluginGenericService.putRelatedDomain(TestDomain.class, 1, 'domain', 1) >> { throw new RuntimeException() }
            response.status == 500
            response.json.error != null

        where:
            domain = 'testdomain'
    }

    void 'Test delete relation method (error saving)'() {
        setup:
            controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)

        when:
            controller.deleteRelatedAdminAction(domain, 1, 'domain', 1)

        then:
            1 * controller.grailsAdminPluginGenericService.deleteRelatedDomain(TestDomain.class, 1, 'domain', 1) >> { throw new RuntimeException() }
            response.status == 500
            response.json.error != null

        where:
            domain = 'testdomain'
    }
}

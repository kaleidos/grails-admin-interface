package net.kaleidos.plugins.admin

import admin.test.TestDomain
import grails.core.DefaultGrailsApplication
import grails.test.mixin.TestFor
import grails.util.Holders
import grails.validation.ValidationException
import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.renderer.GrailsAdminPluginJsonRendererService
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

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
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.grailsAdminPluginDataService.retrieveDomain(TestDomain.class, 1) >> new TestDomain(name:name, year:year)

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${year}\"}"}

            controller.params.id = 1

        when:
            controller.getAdminAction(domain)
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
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.grailsAdminPluginDataService.retrieveDomain(TestDomain.class, 1) >> null

            controller.params.id = 1

        when:
            controller.getAdminAction(domain)
            def result = response.json

        then:
            response.status == 404
            result.error != null

        where:
            domain = 'testdomain'
    }

    void 'Retrieve domain list'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.grailsAdminPluginDataService.listDomain(TestDomain.class) >> [
                new TestDomain(name:"test1", year:2000),
                new TestDomain(name:"test2", year:2001),
                new TestDomain(name:"test3", year:2002),
            ]

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderListAsJson(_) >> "[{\"name\":\"test1\"}, {\"name\":\"test2\"}, {\"name\":\"test3\"}]"

        when:
            controller.getAdminAction(domain)
            def result = response.json

        then:
            response.status == 200
            result.size() == 3

        where:
            domain = 'testdomain'
    }

    void 'Retrieve domain 5 list items sorted'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderListAsJson(_) >> "[{\"name\":\"test1\"}, {\"name\":\"test2\"}, {\"name\":\"test3\"}]"

        when:
            params.sort = 'name'
            params.sort_order = 'asc'
            params.items_by_page = 5
            controller.getAdminAction(domain)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginDataService.list(TestDomain.class, 0, 5, 'name', 'asc')

        where:
            domain = 'testdomain'
    }


    void 'Save domain'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.request.contentType = "application/json"
            controller.request.content = '{"name":"TEST", year: 2000}'

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${yearToString}\"}"}

        when:
            controller.putAdminAction(domain)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginDataService.saveDomain(TestDomain.class, ["name":name, "year":year]) >> new TestDomain(name:name, year:year)

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
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
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
            1 * controller.grailsAdminPluginDataService.saveDomain(TestDomain.class, ["name":name, "year":year]) >> { throw new ValidationException('Validation', validationErrors)}

            response.status == 500
            result.errors != null

        where:
            name = "TEST"
            year = 2000
            domain = 'testdomain'
    }

    void 'Update domain'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.request.contentType = "application/json"
            controller.request.content = '{year: 2003}'

            controller.grailsAdminPluginJsonRendererService = Mock(GrailsAdminPluginJsonRendererService)
            controller.grailsAdminPluginJsonRendererService.renderObjectAsJson(_) >> { "{\"name\":\"${name}\", \"year\":\"${yearToString}\"}"}

            controller.params.id = 1

        when:
            controller.postAdminAction(domain)
            def result = response.json

        then:
            1 * controller.grailsAdminPluginDataService.updateDomain(TestDomain.class, 1, ["year":year]) >> new TestDomain(name:name, year:year)

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
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.request.contentType = "application/json"
            controller.request.content = '{year: 2003}'

            controller.params.id = 1

        when:
            controller.postAdminAction(domain)

        then:
            1 * controller.grailsAdminPluginDataService.updateDomain(TestDomain.class, 1, ["year":year]) >> { throw new RuntimeException() }

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
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.params.id = "1"

        when:
            controller.deleteAdminAction(domain)

        then:
            1 * controller.grailsAdminPluginDataService.deleteDomain(TestDomain.class, 1)
            response.status == 204

        where:
            domain = 'testdomain'
    }

    void 'Batch delete domain'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.params.id = "1,2"

        when:
            controller.deleteAdminAction(domain)

        then:
            1 * controller.grailsAdminPluginDataService.deleteDomain(TestDomain.class, 1)
            1 * controller.grailsAdminPluginDataService.deleteDomain(TestDomain.class, 2)
            response.status == 204

        where:
            domain = 'testdomain'
    }

    void 'Delete domain (runtime exception)'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)
            controller.params.id = "1"

        when:
            controller.deleteAdminAction(domain)

        then:
            1 * controller.grailsAdminPluginDataService.deleteDomain(TestDomain.class, 1) >> { throw new RuntimeException() }
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

    @Unroll
    void 'No configured domain (slug+id)'() {
        when:
            controller."$method"(slug)
            def result = response.json

        then:
            response.status == 404
            result.error != null

        where:
            slug = 'nofound'
            method << ['getAdminAction', 'postAdminAction', 'deleteAdminAction']
    }

    @Unroll
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
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)

        when:
            controller.putRelatedAdminAction(domain, 1, 'domain', 1)

        then:
            1 * controller.grailsAdminPluginDataService.putRelatedDomain(TestDomain.class, 1, 'domain', 1) >> {}
            response.status == 204

        where:
            domain = 'testdomain'
    }

    void 'Test add relation method (error saving)'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)

        when:
            controller.putRelatedAdminAction(domain, 1, 'domain', 1)

        then:
            1 * controller.grailsAdminPluginDataService.putRelatedDomain(TestDomain.class, 1, 'domain', 1) >> { throw new RuntimeException() }
            response.status == 500
            response.json.error != null

        where:
            domain = 'testdomain'
    }

    void 'Test delete relation method (error saving)'() {
        setup:
            controller.grailsAdminPluginDataService = Mock(GrailsAdminPluginDataService)

        when:
            controller.deleteRelatedAdminAction(domain, 1, 'domain', 1)

        then:
            1 * controller.grailsAdminPluginDataService.deleteRelatedDomain(TestDomain.class, 1, 'domain', 1) >> { throw new RuntimeException() }
            response.status == 500
            response.json.error != null

        where:
            domain = 'testdomain'
    }
}

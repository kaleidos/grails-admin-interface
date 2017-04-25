package net.kaleidos.plugins.admin

import admin.test.TestDomain
import grails.core.DefaultGrailsApplication
import net.kaleidos.plugins.admin.config.AdminConfigHolder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.util.Holders
import spock.lang.Shared
import spock.lang.Specification

@TestFor(GrailsAdminPluginUIController)
@Mock(TestDomain)
class GrailsAdminPluginUIControllerSpec extends Specification {
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
        controller.grailsAdminPluginDataService = grails.test.mixin.Mock(GrailsAdminPluginDataService)
    }

    void 'menu'() {
        setup:
            params.slug = 'slug'

        when:
            controller.menu()

        then:
            response.status == 200
            view == '/grailsAdmin/includes/menu'
            model.config == adminConfigHolder
    }

    void 'dashboard'() {
        when:
            controller.dashboard()

        then:
            response.status == 200
            model.config == adminConfigHolder
            view == '/grailsAdmin/dashboard'
    }

    void 'list'() {
        setup:
            def domain = adminConfigHolder.domains['admin.test.TestDomain']

            controller.grailsAdminPluginDataService.count(domain.domainClass) >> 30

            1 * controller.grailsAdminPluginDataService.list(domain.domainClass, _, _, 'sortmy', 'asc') >> {
                [[:]]
            }

        when:
            params.slug = domain.slug
            params.page = 3
            params.sort = 'sortmy'
            params.sort_order = 'asc'
            controller.list()

        then:
            response.status == 200
            view == '/grailsAdmin/list'
            model.objs.size() == 1
            model.domain == domain
            model.currentPage == 3
            model.totalPages == 3

    }

    void 'try to access a wrong domain url to list'() {
        when:
            params.slug = "Bad slug"
            controller.list()

        then:
            response.status == 404
    }

    void 'try to access a wrong domain url when editting'() {
        when:
            params.slug = "Bad slug"
            controller.edit()

        then:
            response.status == 404
    }


    void 'try to access a non existant object when editting'() {

        setup:
            def domain = adminConfigHolder.domains['admin.test.TestDomain']
        when:
            params.slug = domain.slug
            params.id = 9999

            controller.edit()

        then:
            response.status == 404
    }

    void 'try to access a wrong domain url when adding a new object'() {
        when:
            params.slug = "Bad slug"
            controller.add()

        then:
            response.status == 404
    }
}

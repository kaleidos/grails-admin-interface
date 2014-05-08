package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingsHolder
import org.springframework.context.ApplicationContext

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.config.DomainConfig
import net.kaleidos.plugins.admin.GrailsAdminPluginGenericService

import spock.lang.Shared
import spock.lang.Specification
import grails.test.mixin.TestFor

import admin.test.TestDomain

@TestFor(GrailsAdminPluginController)
@Mock(TestDomain)
class GrailsAdminPluginControllerSpec extends Specification {
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
        config.grails.plugin.admin.domains ={
            "admin.test.TestDomain"()
        }

        adminConfigHolder = new AdminConfigHolder(config)
        adminConfigHolder.grailsApplication = grailsApplication
        adminConfigHolder.initialize()
    }

    def setup() {
        controller.adminConfigHolder = adminConfigHolder
        controller.grailsAdminPluginGenericService = Mock(GrailsAdminPluginGenericService)
    }

    void 'menu'() {
        setup:
            params.slug = 'slug'

        when:
            controller.menu()

        then:
            response.status == 200
            view == '/grailsAdmin/includes/menu'
            model.domainClasses.size() == 1
            model.slug == 'slug'
    }

    void 'dashboard'() {
        when:
            controller.dashboard()

        then:
            response.status == 200
            view == '/grailsAdmin/dashboard'
    }

    void 'delete'() {
        setup:
            def domain = adminConfigHolder.domains['admin.test.TestDomain']

        when:
            params.slug = domain.slug
            params.id = id
            controller.delete()

        then:
            response.status == 302
            flash.success != null
            1 * controller.grailsAdminPluginGenericService.deleteDomain(domain.domainClass.clazz, id)

        where:
            id = 2
    }

    void 'list'() {
        setup:
            def domain = adminConfigHolder.domains['admin.test.TestDomain']

            controller.grailsAdminPluginGenericService.count(domain.domainClass.clazz) >> 10

            1 * controller.grailsAdminPluginGenericService.list(domain.domainClass.clazz, 10, 5) >> {
                [[:]]
            }

        when:
            params.slug = domain.slug
            params.page = 3
            controller.list()

        then:
            response.status == 200
            view == '/grailsAdmin/list'
            model.objs.size() == 1
            model.domain == domain
            model.currentPage == 3
            model.totalPages == 2

    }
    
    void 'try to access a wrong domain url to list'() {
        when:
            params.slug = "Bad slug"
            controller.list()

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
    
    void 'try to access a wrong domain url when editting'() {
        when:
            params.slug = "Bad slug"
            controller.edit()

        then:
            response.status == 404
    }


}

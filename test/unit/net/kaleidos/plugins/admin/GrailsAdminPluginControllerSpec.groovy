package net.kaleidos.plugins.admin

import net.kaleidos.plugins.admin.config.AdminConfigHolder

import spock.lang.Specification
import grails.test.mixin.TestFor

import admin.test.TestDomain

@TestFor(GrailsAdminPluginController)
class GrailsAdminPluginControllerSpec extends Specification {

    def setup() {
        controller.adminConfigHolder = Mock(AdminConfigHolder)
    }

    void 'menu'() {
        setup:
            params.slug = 'slug'
            controller.adminConfigHolder.domains >> {
                [:]
            }

        when:
            controller.menu()

        then:
            response.status == 200
            view == '/grailsAdmin/includes/menu'
            model.domainClasses.size() == 0
            model.slug == 'slug'
    }
}

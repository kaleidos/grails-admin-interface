package net.kaleidos.plugins.admin

import grails.test.mixin.*
import spock.lang.*

@TestFor(GrailsAdminPluginCallbackApiController)
class GrailsAdminPluginCallbackApiControllerSpec extends Specification {
    @Unroll
    void 'Test successSave'(){
        when:
            controller."$method"(slug)

        then:
            response.status == 302
            flash.success != null

        where:
            slug = 'slug'
            method << ['successSave', 'successList', 'successNew', 'successDelete']
    }
}

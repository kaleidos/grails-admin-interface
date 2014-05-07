package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.web.sitemesh.GroovyPageLayoutFinder

class GrailsAdminPluginController {
    def objectDefinitionSource
    def adminConfigHolder
    def grailsAdminPluginGenericService
    private static int ITEMS_BY_PAGE = 5

    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }

    def menu(String slug) {
        render view:'/grailsAdmin/includes/menu',  model:[domainClasses: adminConfigHolder.domains.values(), slug: slug]
    }

    def dashboard() {
        render view:'/grailsAdmin/dashboard',  model:[domainClasses: adminConfigHolder.domains.values()]
    }

    def list(String slug, int page) {
        if (!page) {
            page = 1
        }

        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        def objs = grailsAdminPluginGenericService.list(domain.domainClass.clazz, (page -1) * ITEMS_BY_PAGE, ITEMS_BY_PAGE)
        def total = grailsAdminPluginGenericService.count(domain.domainClass.clazz)
        def totalPages = (Math.floor(total / ITEMS_BY_PAGE) as Integer) + 1

        render view:'/grailsAdmin/list',  model:[objs: objs,
                                                 domain: domain,
                                                 currentPage: page,
                                                 totalPages: totalPages]
    }

    def edit() {
        render view:'/grailsAdmin/edit',  model:[]
    }

    def add(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        render view:'/grailsAdmin/add',  model:[domain: domain]
    }

    def addAction(String slug) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        def result = grailsAdminPluginGenericService.saveDomain(config.domainClass.clazz, params)
        if (params["saveAndReturn"]){
            redirect mapping:'list', params:['slug':slug]
            return
        }
        redirect mapping:'add', params:['slug':slug]
    }

}

package net.kaleidos.plugins.admin

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.config.DomainConfig

class GrailsAdminPluginUIController {
    static namespace = "admin"

    static final int ITEMS_BY_PAGE = 10

    AdminConfigHolder adminConfigHolder
    GrailsAdminPluginDataService grailsAdminPluginDataService

    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }

    def menu(String slug) {
        render view:'/grailsAdmin/includes/menu',  model:[config:adminConfigHolder]
    }

    def dashboard() {
        render view:'/grailsAdmin/dashboard',  model:[config:adminConfigHolder]
    }

    def list(String slug, int page) {
        if (!page) {
            page = 1
        }

        def domain = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!domain) {
            response.status = 404
            return
        }
        def objs = grailsAdminPluginDataService.list(domain.domainClass, (page -1) * ITEMS_BY_PAGE as Long, ITEMS_BY_PAGE as Long, params.sort?:'id',  params.sort_order?:'asc')

        if (!objs?.size() && page > 1) {
            redirect(mapping: 'list', params: [slug: slug, page: page - 1])
            return
        }

        def total = grailsAdminPluginDataService.count(domain.domainClass)

        def totalPages = (Math.ceil(total / ITEMS_BY_PAGE) as Integer)

        def model = [:]
        model << [objs:objs]
        model << [domain:domain]
        model << [currentPage:page]
        model << [totalPages:totalPages]
        model << [formType:"list"]
        model << [className:domain.classFullName]
        model << [sort:params.sort]
        model << [sortOrder:params.sort_order]
        render view:'/grailsAdmin/list',  model:model
    }

    def edit(String slug) {
        DomainConfig domain = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!domain || !params.id) {
            response.status = 404
            return
        }

        def object = domain.domainClass.get(params.id)

        if (object) {
            Map model = [:]
            model << [domain:domain]
            model << [object:object]
            model << [formType:"edit"]
            model << [className:domain.classFullName]
            render view:'/grailsAdmin/edit',  model:model
        } else {
            response.status = 404
            return
        }
    }

    def add(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!domain) {
            response.status = 404
            return
        }

        def model = [:]
        model << [domain:domain]
        model << [formType:"create"]
        model << [className:domain.classFullName]
        render view:'/grailsAdmin/add', model:model
    }
}

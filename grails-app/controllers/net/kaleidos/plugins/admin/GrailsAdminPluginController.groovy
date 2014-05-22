package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.web.sitemesh.GroovyPageLayoutFinder
import grails.validation.ValidationException
import grails.converters.JSON

class GrailsAdminPluginController {
    static final int ITEMS_BY_PAGE = 20

    def adminConfigHolder
    def grailsAdminPluginGenericService

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

        if (!domain) {
            response.status = 404
            return
        }
        def objs = grailsAdminPluginGenericService.list(domain.domainClass, (page -1) * ITEMS_BY_PAGE as Long, ITEMS_BY_PAGE as Long, params.sort?:'id',  params.sort_order?:'asc')

        if (!objs?.size() && page > 1) {
            redirect(mapping: 'list', params: [slug: slug, page: page - 1])
            return
        }

        def total = grailsAdminPluginGenericService.count(domain.domainClass)

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

    def edit(String slug, Long id) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!domain) {
            response.status = 404
            return
        }

        def object = domain.domainClass.get(id)

        if (object) {
            def model = [:]
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

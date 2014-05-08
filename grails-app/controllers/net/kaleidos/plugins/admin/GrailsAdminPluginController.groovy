package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.web.sitemesh.GroovyPageLayoutFinder
import grails.validation.ValidationException
import grails.converters.JSON

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

    def delete(String slug, Long id) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        
        if (!domain) {
            response.status = 404
            return
        }

        def success = grailsAdminPluginGenericService.deleteDomain(domain.domainClass.clazz, id)

        if (success) {
            flash.success = g.message(code:'grailsAdminPlugin.action.delete.success')

            redirect(uri: request.getHeader('referer') )
        } else {
            response.status = 404
        
        }
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
        
        def objs = grailsAdminPluginGenericService.list(domain.domainClass.clazz, (page -1) * ITEMS_BY_PAGE, ITEMS_BY_PAGE)

        if (!objs.size() && page > 1) {
            redirect(mapping: 'list', params: [slug: slug, page: page - 1])
            return
        }

        def total = grailsAdminPluginGenericService.count(domain.domainClass.clazz)
        def totalPages = (Math.ceil(total / ITEMS_BY_PAGE) as Integer)

        render view:'/grailsAdmin/list',  model:[objs: objs,
                                                 domain: domain,
                                                 currentPage: page,
                                                 totalPages: totalPages]
    }

    def edit(String slug, Long id) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!domain) {
            response.status = 404
            return
        }

        def object = domain.domainClass.clazz.get(id)

        if (object) {
            render view:'/grailsAdmin/edit',  model:[domain: domain, object: object]
            return
        } else {
            response.status = 404
            return
        }
    }

    def editAction(String slug, Long id) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        def object = domain.domainClass.clazz.get(id)
        if (object) {
             try {
                def result = grailsAdminPluginGenericService.updateDomain(domain.domainClass.clazz, id, params)

                flash.success = g.message(code:"grailsAdminPlugin.edit.success")
                response.status = 200
                render ""
            } catch (ValidationException e) {
                flash.error = g.message(code:"grailsAdminPlugin.add.error")
                response.status = 500
                render e.getErrors() as JSON
            }
        } else {
            flash.error = g.message(code:"grailsAdminPlugin.add.error")
            response.status = 500
            render [] as JSON
        }
    }

    def add(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        render view:'/grailsAdmin/add',  model:[domain: domain]
    }

    def addAction(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)

        try {
            def result = grailsAdminPluginGenericService.saveDomain(domain.domainClass.clazz, params)
            flash.success = g.message(code:"grailsAdminPlugin.add.success")
            response.status = 200
            render ""
            return


        } catch (ValidationException e) {
            flash.error = g.message(code:"grailsAdminPlugin.add.error")
            response.status = 500
            render e.getErrors() as JSON
        }

    }

}

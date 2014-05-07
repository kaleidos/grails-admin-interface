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

    def delete(String slug, Long id) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)

        grailsAdminPluginGenericService.deleteDomain(domain.domainClass.clazz, id)

        flash.success = g.message(code:'grailsAdminPlugin.action.delete.success')

        redirect(uri: request.getHeader('referer') )
    }

    def list(String slug, int page) {
        if (!page) {
            page = 1
        }

        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
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
        def object = domain.domainClass.clazz.get(id)

        if (object) {
            render view:'/grailsAdmin/edit',  model:[domain: domain, object: object]
            return
        } else {
            redirect mapping:'list', params:['slug':slug]
            return
        }
    }

    def editAction(String slug, Long id) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        def object = domain.domainClass.clazz.get(id)
        if (object) {
            def result = grailsAdminPluginGenericService.updateDomain(domain.domainClass.clazz, id, params)

            if (!result.hasErrors()) {
                flash.success = g.message(code:"grailsAdminPlugin.edit.success")
                if (params["saveAndReturn"]){
                    redirect mapping:'list', params:['slug':slug]
                    return
                }
                redirect mapping:'edit', params:['slug':slug, 'id':id]
            } else {
                flash.error = g.message(code:"grailsAdminPlugin.edit.error")
                render view:'/grailsAdmin/edit',  model:[domain: domain, object: result]
            }
        } else {
            redirect mapping:'list', params:['slug':slug]
            return
        }
    }

    def add(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        render view:'/grailsAdmin/add',  model:[domain: domain]
    }

    def addAction(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        def result = grailsAdminPluginGenericService.saveDomain(domain.domainClass.clazz, params)

        if (!result.hasErrors()) {
            flash.success = g.message(code:"grailsAdminPlugin.add.success")
            if (params["saveAndReturn"]){
                redirect mapping:'list', params:['slug':slug]
                return
            }
            redirect mapping:'add', params:['slug':slug]
        } else {
            flash.error = g.message(code:"grailsAdminPlugin.add.error")
            render view:'/grailsAdmin/add',  model:[domain: domain]
        }

    }

}

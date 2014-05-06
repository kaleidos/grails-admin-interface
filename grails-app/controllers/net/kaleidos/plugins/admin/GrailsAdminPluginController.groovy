package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.web.sitemesh.GroovyPageLayoutFinder

class GrailsAdminPluginController {
    def objectDefinitionSource
    def adminConfigHolder
    def grailsAdminPluginGenericService

    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }

    def menu() {
        render view:'/grailsAdmin/includes/menu',  model:[domainClasses: adminConfigHolder.domains.values()]
    }

    def dashboard() {
        render view:'/grailsAdmin/dashboard',  model:[domainClasses: adminConfigHolder.domains.values()]
    }

    def list(String slug) {
        def domain = adminConfigHolder.getDomainConfigBySlug(slug)
        def objs = grailsAdminPluginGenericService.list(domain.domainClass.clazz)

        render view:'/grailsAdmin/list',  model:[objs: objs, domain: domain]
    }

    def edit() {
        render view:'/grailsAdmin/edit',  model:[]
    }

    def add() {
        render view:'/grailsAdmin/add',  model:[]
    }

}

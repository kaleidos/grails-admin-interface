package net.kaleidos.plugins.admin

import grails.converters.JSON

import org.springframework.security.access.annotation.Secured

@Secured(["ROLE_ADMIN"])
class GrailsAdminPluginApiController {
    def objectDefinitionSource
    def adminConfigHolder
    def grailsAdminPluginGenericService

    def listDomains() {
        render adminConfigHolder.domainClasses as JSON
    }

    def getAdminAction(String domain, Long id) {
        def config = _resolve(domain)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return;
        }

        def result
        if (id) {
            result = grailsAdminPluginGenericService.retrieveDomain(config.domainClass.clazz, id)
            if (!result) {
                response.status = 404
                render(["error":"Entity not found"] as JSON)
                return;
            }
        } else {
            result = grailsAdminPluginGenericService.listDomains(config.domainClass.clazz)
        }
        render result as JSON
    }

    def postAdminAction(String domain, Long id) {
        def config = _resolve(domain)
        render result as JSON
    }

    def deleteAdminAction(String adminController, String adminAction, Long id) {
        log.debug ">> DELETE: ${params}"
        render "DELETE ${params}"
    }

    def putAdminAction(String adminController, String adminAction, Long id) {
        log.debug ">> PUT: ${params}"
        render "PUT ${params}"
    }

    def menu() {
        render view:'/grailsAdmin/includes/menu',  model:[]
    }

    private _resolve(String adminController) {
        def domainClasses  = adminConfigHolder.domainClasses
        def className = domainClasses.find {
            def className = it.tokenize(".")[-1]
            def camelClassName = className[0].toLowerCase() + className.substring(1)
            return camelClassName == adminController
        }
        return adminConfigHolder.domains[className]
    }
}

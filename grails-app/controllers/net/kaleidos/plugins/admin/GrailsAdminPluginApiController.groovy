package net.kaleidos.plugins.admin

import grails.converters.JSON

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

    def putAdminAction(String domain) {
        def config = _resolve(domain)
        def result = grailsAdminPluginGenericService.saveDomain(config.domainClass.clazz, request.JSON)
        render result as JSON
    }

    def postAdminAction(String domain, Long id) {
        def config = _resolve(domain)
        def result = grailsAdminPluginGenericService.updateDomain(config.domainClass.clazz, id, request.JSON)
        render result as JSON
    }

    def deleteAdminAction(String domain, Long id) {
        def config = _resolve(domain)
        grailsAdminPluginGenericService.deleteDomain(config.domainClass.clazz, id)
        response.status = 204
        render ""
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

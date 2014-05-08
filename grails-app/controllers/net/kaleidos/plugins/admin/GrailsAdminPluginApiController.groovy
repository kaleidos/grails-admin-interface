package net.kaleidos.plugins.admin

import grails.converters.JSON
import grails.validation.ValidationException

class GrailsAdminPluginApiController {
    def objectDefinitionSource
    def adminConfigHolder
    def grailsAdminPluginGenericService
    def grailsAdminPluginBuilderService

    def listDomains() {
        render adminConfigHolder.domainClasses as JSON
    }

    def getAdminAction(String slug, Long id) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        def result
        def renderedResult
        if (id) {
            result = grailsAdminPluginGenericService.retrieveDomain(config.domainClass.clazz, id)
            if (!result) {
                response.status = 404
                render(["error":"Entity not found"] as JSON)
                return
            }
            renderedResult = grailsAdminPluginBuilderService.renderObjectAsJson(result)
        } else {
            result = grailsAdminPluginGenericService.listDomain(config.domainClass.clazz)
            renderedResult = grailsAdminPluginBuilderService.renderListAsJson(result)
        }

        render renderedResult
    }

    def putAdminAction(String slug) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        def result = [:]
        try {
            result = grailsAdminPluginGenericService.saveDomain(config.domainClass.clazz, request.JSON)
        } catch (ValidationException e) {
            response.status = 500
            result = e.getErrors()
        }
        render result as JSON
    }

    def postAdminAction(String slug, Long id) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        def result = [:]
        try {
            result = grailsAdminPluginGenericService.updateDomain(config.domainClass.clazz, id, request.JSON)
        } catch (ValidationException e) {
            response.status = 500
            result = e.getErrors()
        } catch (RuntimeException e) {
            response.status = 500
            result = [error: e.message]
        }
        render result as JSON
    }

    def deleteAdminAction(String slug, Long id) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        try {
            grailsAdminPluginGenericService.deleteDomain(config.domainClass.clazz, id)
        } catch (RuntimeException e) {
            response.status = 500
            def result = [error: e.message]
            render result as JSON
            return
        }
        response.status = 204
        render ""
    }
}

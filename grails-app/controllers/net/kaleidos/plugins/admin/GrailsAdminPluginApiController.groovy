package net.kaleidos.plugins.admin

import grails.converters.JSON
import grails.validation.ValidationException

class GrailsAdminPluginApiController {
    def adminConfigHolder
    def grailsAdminPluginGenericService
    def grailsAdminPluginJsonRendererService

    private static int ITEMS_BY_PAGE = 20

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
            result = grailsAdminPluginGenericService.retrieveDomain(config.domainClass, id)
            if (!result) {
                response.status = 404
                render(["error":"Entity not found"] as JSON)
                return
            }
            renderedResult = grailsAdminPluginJsonRendererService.renderObjectAsJson(result)
        } else {
            def page = params.page

            if (!page) {
                page = 1
            }

            result = grailsAdminPluginGenericService.list(config.domainClass, (page -1) * ITEMS_BY_PAGE as Long, ITEMS_BY_PAGE as Long, params.sort,  params.sort_order)
            renderedResult = grailsAdminPluginJsonRendererService.renderListAsJson(result)
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
            result = grailsAdminPluginGenericService.saveDomain(config.domainClass, request.JSON)
        } catch (ValidationException e) {
            response.status = 500
            render e.getErrors() as JSON
            return
        } catch (RuntimeException e) {
            log.debug e.message, e
            response.status = 500
            result = [error: e.message]
            render result as JSON
            return
        }

        render grailsAdminPluginJsonRendererService.renderObjectAsJson(result)
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
            result = grailsAdminPluginGenericService.updateDomain(config.domainClass, id, request.JSON)
        } catch (ValidationException e) {
            response.status = 500
            render e.getErrors() as JSON
            return
        } catch (RuntimeException e) {
            log.debug e.message, e
            response.status = 500
            result = [error: e.message]
            render result as JSON
            return
        }

        render grailsAdminPluginJsonRendererService.renderObjectAsJson(result)
    }

    def deleteAdminAction(String slug, Long id) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        try {
            grailsAdminPluginGenericService.deleteDomain(config.domainClass, id)
        } catch (RuntimeException e) {
            log.debug e.message, e
            response.status = 500
            def result = [error: e.message]
            render result as JSON
            return
        }
        response.status = 204
        render ""
    }

    def deleteRelatedAdminAction(String slug, Long id, String propertyName, Long id2) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }


        try {
            grailsAdminPluginGenericService.deleteRelatedDomain(config.domainClass, id, propertyName, id2)
        } catch (RuntimeException e) {
            log.debug e.message, e
            response.status = 500
            def result = [error: e.message]
            render result as JSON
            return
        }
        response.status = 204
        render ""
    }


    def putRelatedAdminAction(String slug, Long id, String propertyName, Long id2) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        try {
            grailsAdminPluginGenericService.putRelatedDomain(config.domainClass, id, propertyName, id2)
        } catch (RuntimeException e) {
            log.debug e.message, e
            response.status = 500
            def result = [error: e.message]
            render result as JSON
            return
        }
        response.status = 204
        render ""
    }

}

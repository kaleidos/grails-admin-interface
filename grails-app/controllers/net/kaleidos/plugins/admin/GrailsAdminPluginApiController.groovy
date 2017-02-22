package net.kaleidos.plugins.admin

import grails.converters.JSON
import grails.validation.ValidationException
import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.renderer.GrailsAdminPluginJsonRendererService

class GrailsAdminPluginApiController {
    static namespace = "admin"

    AdminConfigHolder adminConfigHolder
    GrailsAdminPluginDataService grailsAdminPluginDataService
    GrailsAdminPluginJsonRendererService grailsAdminPluginJsonRendererService

    private static int ITEMS_BY_PAGE = 20

    def listDomains() {
        render adminConfigHolder.domainClasses as JSON
    }

    def countAdminAction(String slug) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        def total = grailsAdminPluginDataService.count(config.domainClass)

        render(["total":total] as JSON)
    }

    def getAdminAction(String slug) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)

        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        def result
        String renderedResult
        if (params?.id) {
            result = grailsAdminPluginDataService.retrieveDomain(config.domainClass, params?.id)
            if (!result) {
                response.status = 404
                render(["error":"Entity not found"] as JSON)
                return
            }
            renderedResult = grailsAdminPluginJsonRendererService.renderObjectAsJson(result)
        } else {
            def page = params.page as Long

            if (!page) {
                page = 1
            }

            def itemsByPage = ITEMS_BY_PAGE
            def maxItemsPerPage = 1000
            def paramItemsByPage = params.items_by_page as Integer

            if (paramItemsByPage && paramItemsByPage < maxItemsPerPage) {
                itemsByPage = paramItemsByPage
            } else if (paramItemsByPage > maxItemsPerPage) {
                itemsByPage = maxItemsPerPage
            }

            result = grailsAdminPluginDataService.list(config.domainClass, (page -1) * itemsByPage as Long, itemsByPage as Long, params.sort,  params.sort_order)
            renderedResult = grailsAdminPluginJsonRendererService.renderListAsJson(result)
        }

        render text:renderedResult
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
            result = grailsAdminPluginDataService.saveDomain(config.domainClass, request.JSON)
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

    def postAdminAction(String slug) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        def result = [:]
        try {
            result = grailsAdminPluginDataService.updateDomain(config.domainClass, params.id, request.JSON)
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

    def deleteAdminAction(String slug) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        if (params?.id) {
            def errorMessages = ""
            def ids = params.id.split(",")

            ids.each {
                try {
                    grailsAdminPluginDataService.deleteDomain(config.domainClass, Long.parseLong(it))
                } catch (RuntimeException e) {
                    log.debug e.message, e
                    errorMessages += "${g.message(code:'grailsAdminPlugin.action.delete.error', args:[config.domainClass.simpleName, it])}. \n"
                }
            }
            if (errorMessages) {
                response.status = 500
                def result = [error:errorMessages]
                render result as JSON
                return
            }
        } else {
            response.status = 500
            def result = [error:g.message(code:'grailsAdminPlugin.action.delete.error.none')]
            render result as JSON
            return
        }
        response.status = 204
        render ""
    }

    def deleteRelatedAdminAction(String slug, Object id, String propertyName, Object id2) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        try {
            grailsAdminPluginDataService.deleteRelatedDomain(config.domainClass, id, propertyName, id2)
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


    def putRelatedAdminAction(String slug, Object id, String propertyName, Object id2) {
        def config = adminConfigHolder.getDomainConfigBySlug(slug)
        if (!config) {
            response.status = 404
            render(["error":"Domain no configured"] as JSON)
            return
        }

        try {
            grailsAdminPluginDataService.putRelatedDomain(config.domainClass, id, propertyName, id2)
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

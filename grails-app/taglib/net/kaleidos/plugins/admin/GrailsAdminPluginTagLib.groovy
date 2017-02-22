package net.kaleidos.plugins.admin

import grails.web.mapping.LinkGenerator
import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.renderer.GrailsAdminPluginHtmlRendererService


class GrailsAdminPluginTagLib {
    static namespace = 'gap'

    GrailsAdminPluginHtmlRendererService grailsAdminPluginHtmlRendererService
    AdminConfigHolder adminConfigHolder
    def grailsResourceLocator
    LinkGenerator grailsLinkGenerator

    def editFormFields = { attrs ->
        def editWidgetProperties = [:]

        if (attrs.editWidgetProperties instanceof Map) {
            editWidgetProperties = attrs.editWidgetProperties
        }
        editWidgetProperties << [disallowRelationships: attrs.disallowRelationships]

        out << grailsAdminPluginHtmlRendererService.renderEditFormFields(attrs.object, editWidgetProperties)
    }

    def createFormFields = { attrs ->
        Map createWidgetProperties = [:]

        if (attrs.createWidgetProperties instanceof Map) {
            createWidgetProperties = attrs.createWidgetProperties
        }
        createWidgetProperties << [disallowRelationships: attrs.disallowRelationships]

        out << grailsAdminPluginHtmlRendererService.renderCreateFormFields(attrs.className, createWidgetProperties)
    }

    def widgetBeforeForm = { attrs ->
        def createWidgetProperties = [:]
        if (attrs.createWidgetProperties instanceof Map) {
            createWidgetProperties = attrs.createWidgetProperties
        }
        createWidgetProperties << [disallowRelationships: attrs.disallowRelationships]

        def result = grailsAdminPluginHtmlRendererService.renderBeforeForm(attrs.className, createWidgetProperties)
        if (result) {
            out << result
        }
    }

    def widgetAfterForm = { attrs ->
        def createWidgetProperties = [:]

        if (attrs.createWidgetProperties instanceof Map) {
            createWidgetProperties = attrs.createWidgetProperties
        }
        createWidgetProperties << [disallowRelationships: attrs.disallowRelationships]

        def result = grailsAdminPluginHtmlRendererService.renderAfterForm(attrs.className, createWidgetProperties)
        if (result) {
            out << result
        }
    }

    def listLine = { attrs ->
        def domain = adminConfigHolder.getDomainConfig(attrs.object)
        def dataUrl = ""
        if (attrs.object.id) {
            dataUrl = g.createLink(absolute:true, mapping:"grailsAdminEdit", params:[slug: domain.slug, id: attrs.object.id])
        }

        out << "<tr data-url='$dataUrl'>"

        out << "<td class=\"js-list-delete\">"
        if (attrs.object.id) {
            out << "<input type=\"checkbox\" class=\"js-list-delete\" data-element-id=\"${attrs.object.id}\"/>"
        }
        out << "</td>"

        out << grailsAdminPluginHtmlRendererService.renderListLine(attrs.className, attrs.object)
        out << "</tr>"
    }


    def listTitles = { attrs ->
        out << grailsAdminPluginHtmlRendererService.renderListTitle(attrs.className, attrs.sort, attrs.sortOrder)
    }

    def layoutCss = { attrs ->
        def buildClosure = { Map assetProperties->
            def assetUrl = grailsLinkGenerator.resource(assetProperties)
            out << "<link href=\"${assetUrl}\" rel=\"stylesheet\"></link>"
        }
        getViewResources("css").each(buildClosure)
        grailsAdminPluginHtmlRendererService.doWithAssetType(attrs.formType, attrs.className, "css", buildClosure)
    }

    def layoutJs = { attrs->
        def buildClosure = { Map assetProperties->
            def assetUrl = grailsLinkGenerator.resource(assetProperties)
            out << "<script src=\"${assetUrl}\"></script>"
        }
        getViewResources("js").each(buildClosure)
        grailsAdminPluginHtmlRendererService.doWithAssetType(attrs.formType, attrs.className, "js", buildClosure)
    }

    def layoutHandlebers = { attrs->
        def buildClosure = { Map assetProperties->
            if (grailsResourceLocator.findResourceForURI(assetProperties.file)) {
                def file = grailsResourceLocator.findResourceForURI(assetProperties.file).getFile()
                def id = file.name.substring(0, file.name.lastIndexOf("."))
                out << "<script id=\"${id}\" type=\"text/x-handlebars-template\">${file.getText()}</script>"
            }
        }

        grailsAdminPluginHtmlRendererService.doWithAssetType(attrs.formType, attrs.className, "handlebars", buildClosure)
    }

    def pagination = { attrs->
        def domain = attrs.domain
        def totalPages = attrs.totalPages
        def currentPage = attrs.currentPage

        def htmlclass = { (it == currentPage)?"class='active'":"" }
        def pages = (1..totalPages).toList()

        out << "<ul class='pagination'>"

        if (currentPage > 1) {
            out << "<li>${g.link('«', mapping:'grailsAdminList', params: [slug: domain.slug, page: currentPage - 1])}</li>"
        }

        if (totalPages >= 20) {
            pages = (1..5).toList()
            pages = pages + (currentPage-2 .. ((currentPage + 2 > totalPages)?totalPages:(currentPage + 2))).toList()
            pages = pages + (((totalPages-4 < 0)?0:(totalPages-4)) .. totalPages).toList()
            pages.sort().unique()
            pages = pages.findAll { it > 0 }
            pages = pages.inject([]) { result, i ->
                if (result.size() > 0 && i - result[-1] > 1) {
                    result + ['...', i]
                } else {
                    result + [i]
                }
            }
        }

        pages.each {
            if(it instanceof Integer) {
                out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
            } else {
                out << "<li><a>$it</a></li>"
            }
        }

        if (currentPage < totalPages) {
            out << "<li>${g.link('»', mapping:'grailsAdminList', params: [slug: domain.slug, page: currentPage + 1])}</li>"
        }
        out << "</ul>"
    }

    List<Map> getViewResources(String type){
        def result = []
        if (type == "css") {
            result << 'grails-admin/libs/bootstrap/css/bootstrap.css'
            result << 'grails-admin/libs/bootstrap/css/bootstrap-theme.css'
            result << 'grails-admin/css/main.css'
        }

        if (type == "js") {
            result << 'grails-admin/libs/jquery.js'
            result << 'grails-admin/libs/lodash.js'
            result << 'grails-admin/libs/bootstrap/js/bootstrap.js'
            result << 'grails-admin/libs/injector.js'
            result << 'grails-admin/libs/parsley.remote.js'
            result << 'grails-admin/libs/jquery.serializeObject.js'
            result << 'grails-admin/libs/handlebars.js'
            result << 'grails-admin/js/main.js'
            result << 'grails-admin/js/services/templateService.js'
            result << 'grails-admin/js/services/paginationService.js'
            result << 'grails-admin/js/views/formView.js'
            result << 'grails-admin/js/views/deleteModalView.js'
            result << 'grails-admin/js/views/listView.js'
            result << 'grails-admin/js/general.js'
        }
        return result.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }
}

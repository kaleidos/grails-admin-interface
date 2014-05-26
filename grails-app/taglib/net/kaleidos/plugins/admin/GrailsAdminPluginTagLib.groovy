package net.kaleidos.plugins.admin

class GrailsAdminPluginTagLib {
    static namespace = 'gap'

    def grailsAdminPluginHtmlRendererService
    def adminConfigHolder
    def grailsResourceLocator


    def editFormFields = { attrs ->
        def editWidgetProperties = [:]

        if (attrs.editWidgetProperties instanceof Map) {
            editWidgetProperties = attrs.editWidgetProperties
        }
        editWidgetProperties << [disallowRelationships: attrs.disallowRelationships]

        out << grailsAdminPluginHtmlRendererService.renderEditFormFields(attrs.object, editWidgetProperties)
    }

    def createFormFields = { attrs ->
        def createWidgetProperties = [:]

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
        out << grailsAdminPluginHtmlRendererService.renderListLine(attrs.object)
    }

    def listTitles = { attrs ->
        out << grailsAdminPluginHtmlRendererService.renderListTitle(attrs.className, attrs.sort, attrs.sortOrder)
    }

    def layoutCss = { attrs ->
        def buildClosure = {
            if (grailsResourceLocator.findResourceForURI(it)) {
                out << "<link href=\"${request.contextPath}${grailsResourceLocator.findResourceForURI(it).getPath()}\" rel=\"stylesheet\"></link>"
            }
        }
        getViewResources("css").each(buildClosure)
        grailsAdminPluginHtmlRendererService.doWithAssetType(attrs.formType, attrs.className, "css", buildClosure)
    }

    def layoutJs = { attrs->
        def buildClosure = {
            if (grailsResourceLocator.findResourceForURI(it)) {
                out << "<script src=\"${request.contextPath}${grailsResourceLocator.findResourceForURI(it).getPath()}\"></script>"
            }
        }
        getViewResources("js").each(buildClosure)
        grailsAdminPluginHtmlRendererService.doWithAssetType(attrs.formType, attrs.className, "js", buildClosure)
    }

    def pagination = { attrs->
        def domain = attrs.domain
        def totalPages = attrs.totalPages
        def currentPage = attrs.currentPage

        def htmlclass = { (it == currentPage)?"class='active'":"" }

        out << "<ul class='pagination'>"

        if (currentPage > 1) {
            out << "<li>${g.link('«', mapping:'grailsAdminList', params: [slug: domain.slug, page: currentPage - 1])}</li>"
        }

        if (totalPages < 20) {
            totalPages.times {
                out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
            }
        } else {
            // Print first 5 pages
            (1..5).each {
                out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
            }

            if (currentPage >= 5 && currentPage <= 7) {
                (6 .. currentPage +2).each {
                    out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
                }
                out << "<li><a>...</a></li>"
            } else {
                out << "<li><a>...</a></li>"
            }

            // Print current and two before and two after

            if (currentPage > 7 && currentPage < totalPages - 8) {
                (currentPage-2 .. currentPage +2).each {
                    out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
                }
            }

            if (currentPage >= totalPages -8 && currentPage <= totalPages -4) {
                (currentPage-2 .. totalPages -5).each {
                    out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
                }
            } else if (currentPage <= totalPages-4) {
                out << "<li><a>...</a></li>"
            }

            // Print last 5 pages
            (totalPages-4 .. totalPages).each {
                out << "<li ${htmlclass(it)}>${g.link("$it",mapping:'grailsAdminList', params: [slug: domain.slug, page: it])}</li>"
            }
        }

        if (currentPage < totalPages) {
            out << "<li>${g.link('»', mapping:'grailsAdminList', params: [slug: domain.slug, page: currentPage + 1])}</li>"
        }
        out << "</ul>"
    }

    List<String> getViewResources(String type){
        def result = []
        if (type == "css") {
            result << 'grails-admin/libs/bootstrap/css/bootstrap.css'
            result << 'grails-admin/libs/bootstrap/css/bootstrap-theme.css'
            result << 'grails-admin/css/main.css'
        }

        if (type == "js") {
            result << 'grails-admin/libs/jquery/jquery.js'
            result << 'grails-admin/libs/bootstrap/js/bootstrap.js'
            result << 'grails-admin/libs/injectorJS/injector.js'
            result << 'grails-admin/libs/parsleyjs/parsley.remote.js'
            result << 'grails-admin/libs/serializeObject.js'
            result << 'grails-admin/js/main.js'
            result << 'grails-admin/js/views/formView.js'
            result << 'grails-admin/js/views/deleteModalView.js'
            result << 'grails-admin/js/general.js'
        }

        return result
    }
}

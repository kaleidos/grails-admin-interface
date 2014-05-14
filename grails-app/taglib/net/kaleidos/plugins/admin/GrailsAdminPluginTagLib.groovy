package net.kaleidos.plugins.admin

class GrailsAdminPluginTagLib {
    static namespace = 'gap'

    def grailsAdminPluginBuilderService
    def adminConfigHolder
    def grailsResourceLocator


    /*
     <gap:editForm
                object="${new conferences.Conference(name:'aaaa')}"

                editFormProperties="${[
                    'class':'main-form',
                    'style':'border: 1px solid black'
                ]}"

                editWidgetProperties="${[
                    'class':'form-control',
                    'style':'color: red'
                ]}"

                />

    */


    def editFormFields = { attrs ->
        def editWidgetProperties = [:]

        if (attrs.editWidgetProperties instanceof Map) {
            editWidgetProperties = attrs.editWidgetProperties
        }


        out << grailsAdminPluginBuilderService.renderEditFormFields(attrs.object, editWidgetProperties)
    }

    def createFormFields = { attrs ->
        def createWidgetProperties = [:]

        if (attrs.createWidgetProperties instanceof Map) {
            createWidgetProperties = attrs.createWidgetProperties
        }


        out << grailsAdminPluginBuilderService.renderCreateFormFields(attrs.className, createWidgetProperties)
    }

    def listLine = { attrs ->
        out << grailsAdminPluginBuilderService.renderListLine(attrs.object)
    }

    def listTitles = { attrs ->
        out << grailsAdminPluginBuilderService.renderListTitle(attrs.className)
    }

    def layoutCss = { attrs ->
        def buildClosure = {
            if (grailsResourceLocator.findResourceForURI(it)) {
                out << "<link href=\"${request.contextPath}${grailsResourceLocator.findResourceForURI(it).getPath()}\" rel=\"stylesheet\"></link>"
            }
        }
        adminConfigHolder.getViewResources("css").each(buildClosure)
        grailsAdminPluginBuilderService.doWithAssetType(attrs.formType, attrs.className, "css", buildClosure)
    }

    def layoutJs = { attrs->
        def buildClosure = {
            if (grailsResourceLocator.findResourceForURI(it)) {
                out << "<script src=\"${request.contextPath}${grailsResourceLocator.findResourceForURI(it).getPath()}\"></script>"
            }
        }
        adminConfigHolder.getViewResources("js").each(buildClosure)
        grailsAdminPluginBuilderService.doWithAssetType(attrs.formType, attrs.className, "js", buildClosure)
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
}

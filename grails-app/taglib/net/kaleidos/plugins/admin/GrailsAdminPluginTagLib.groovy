package net.kaleidos.plugins.admin

class GrailsAdminPluginTagLib {
    static namespace = 'gap'

    def grailsAdminPluginBuilderService
    def adminConfigHolder


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
            out << "<link href=\"${g.resource(file: it, plugin: 'admin')}\" rel=\"stylesheet\"></link>"
        }
        adminConfigHolder.getViewResources("css").each(buildClosure)
        grailsAdminPluginBuilderService.doWithAssetType(attrs.formType, attrs.className, "css", buildClosure)
    }

    def layoutJs = { attrs->
        def buildClosure = {
            out << "<script src=\"${g.resource(file: it, plugin: 'admin')}\"></script>"
        }
        adminConfigHolder.getViewResources("js").each(buildClosure)
        grailsAdminPluginBuilderService.doWithAssetType(attrs.formType, attrs.className, "js", buildClosure)
    }
}

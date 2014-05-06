package net.kaleidos.plugins.admin

class GrailsAdminPluginTagLib {
    static namespace = 'gap'
    def grailsAdminPluginBuilderService


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

}

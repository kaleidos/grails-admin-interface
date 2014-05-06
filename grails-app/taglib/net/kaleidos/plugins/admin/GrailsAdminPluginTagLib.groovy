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


    def editForm = { attrs ->
        def editFormProperties = [:]
        def editWidgetProperties = [:]

        if (attrs.editFormProperties instanceof Map) {
            editFormProperties = attrs.editFormProperties
        }

        if (attrs.editWidgetProperties instanceof Map) {
            editWidgetProperties = attrs.editWidgetProperties
        }


        out << grailsAdminPluginBuilderService.renderEditForm(attrs.object, editFormProperties, editWidgetProperties)
    }

    def createForm = { attrs ->
        def createFormProperties = [:]
        def createWidgetProperties = [:]

        if (attrs.createFormProperties instanceof Map) {
            createFormProperties = attrs.createFormProperties
        }

        if (attrs.createWidgetProperties instanceof Map) {
            createWidgetProperties = attrs.createWidgetProperties
        }


        out << grailsAdminPluginBuilderService.renderCreateForm(attrs.className, createFormProperties, createWidgetProperties)
    }

    def listLine = { attrs ->
        out << grailsAdminPluginBuilderService.renderListLine(attrs.object)
    }

}

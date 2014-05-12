package net.kaleidos.plugins.admin.builder

import groovy.json.JsonBuilder

class GrailsAdminPluginBuilderService {
    def adminConfigHolder
    def grailsAdminPluginWidgetService

    String renderEditFormFields(Object object, Map editWidgetProperties=[:]){
        return _renderFormFields("edit", object, editWidgetProperties)
    }

    String renderCreateFormFields(String className, Map createWidgetProperties=[:]){
        def objectClass = this.getClass().classLoader.loadClass(className)
        def object = objectClass?.newInstance()
        return _renderFormFields("create", object, createWidgetProperties)
    }

    String _renderFormFields(String formType, Object object, Map widgetProperties){
        StringBuilder html = new StringBuilder()
        if (object) {
            List properties = adminConfigHolder.getDomainConfig(object).getProperties(formType)
            properties.each{propertyName ->
                def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, null, widgetProperties)
                html.append("<div class=\"form-group\">")
                html.append("<label for=\"${propertyName.encodeAsHTML()}\">${propertyName.capitalize().encodeAsHTML()}</label>")
                html.append(widget.render())
                html.append("</div>")
            }
        }
        return html
    }


    //list

    String renderListLine(Object object){
        def config = adminConfigHolder.getDomainConfig(object)
        List properties = config.getProperties("list")
        StringBuilder html = new StringBuilder()
        properties.each{propertyName ->
            def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName)
            html.append("<td>")
            def val = object."${propertyName}"

            if (val instanceof Boolean) {
                def label = 'label-success'
                if (!val) {
                    label = 'label-danger'
                }

                html.append("<span class=\"label ${label}\">")
                html.append(val.encodeAsHTML())
                html.append("</span>")
            } else {
                html.append(val?val.encodeAsHTML():'&nbsp;')
            }


            // TODO: Who has to decide how to encode? Widget or this method?


            html.append("</td>")
        }
        return html
    }

    String renderListTitle(String className){
        def objectClass = this.getClass().classLoader.loadClass(className)
        def object = objectClass?.newInstance()

        List properties = adminConfigHolder.getDomainConfig(object).getProperties("list")
        StringBuilder html = new StringBuilder()

        properties.each{ propertyName ->
            def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName)

            html.append("<th>")
            html.append(propertyName)
            html.append("</th>")
        }

        return html
    }



    // JSON

    String renderListAsJson(List list) {
        def resultList = []
        list.each { element ->
            resultList << _getInfoForJson(element)
        }

        return new JsonBuilder(resultList).toString()
    }

    def _getInfoForJson(object) {
        List properties = adminConfigHolder.getDomainConfig(object).getProperties("list")
        def result = [:]

        if (object.id) {
            result.id = object.id
        }

        properties.each { propertyName ->
            def val = object."${propertyName}"
            if (val) {
                def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName)
                result << ["$propertyName":widget.renderAsJson()]
            }
        }

        return result
    }

    String renderObjectAsJson(Object object) {
        def result = _getInfoForJson(object)
        return new JsonBuilder(result).toString()
    }

}

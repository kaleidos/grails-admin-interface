package net.kaleidos.plugins.admin.builder

class GrailsAdminPluginBuilderService {
    def adminConfigHolder
    def grailsAdminPluginWidgetService

    static final DEFAULT_PROPERTIES_EDIT_FORM = ["class":"main-form"]
    static final DEFAULT_PROPERTIES_EDIT_WIDGET = ["class":"form-control"]

    String renderEditForm(Object object, Map editFormProperties=[:], Map editWidgetProperties=[:]){
        List properties = adminConfigHolder.getDomainConfig(object).getProperties("edit")
        StringBuilder html = new StringBuilder()

        def props = [:]
        props.putAll(DEFAULT_PROPERTIES_EDIT_FORM)
        props.putAll(editFormProperties)
        html.append("<form")
        props.each {key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append(">")



        properties.each{propertyName ->
            def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, null, DEFAULT_PROPERTIES_EDIT_WIDGET)
            html.append("<div class=\"form-group\">")
            html.append("<label for=\"${propertyName.encodeAsHTML()}\">${propertyName.capitalize().encodeAsHTML()}</label>")
            html.append(widget.render())
            html.append("<div>")
        }
        html.append("</form>")
        return html
    }


    String renderListLine(Object object){
        List properties = adminConfigHolder.getDomainConfig(object).getProperties("list")
        StringBuilder html = new StringBuilder()
        properties.each{propertyName ->
            def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName)
            html.append("<td>")
            def val = object."${propertyName}"
            html.append(val?val.encodeAsHTML():'&nbsp;')
            html.append("</td>")
        }
        return html
    }
}

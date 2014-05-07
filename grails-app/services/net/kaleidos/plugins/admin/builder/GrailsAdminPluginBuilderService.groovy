package net.kaleidos.plugins.admin.builder

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


    String renderListLine(Object object){
        List properties = adminConfigHolder.getDomainConfig(object).getProperties("list")
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
}

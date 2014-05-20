package net.kaleidos.plugins.admin.builder

import groovy.json.JsonBuilder

class GrailsAdminPluginBuilderService {
    def adminConfigHolder
    def grailsAdminPluginWidgetService
    def grailsApplication
    def grailsLinkGenerator

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
            def domainConfig = adminConfigHolder.getDomainConfig(object)
            List properties = domainConfig.getProperties(formType)
            Map customWidgets = domainConfig.getCustomWidgets(formType)

            properties.each{propertyName ->
                def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, customWidgets?."$propertyName", widgetProperties)

                html.append("<div class=\"form-group\">")
                html.append("<label for=\"${propertyName.encodeAsHTML()}\">")
                html.append(propertyName.capitalize().encodeAsHTML())

                if (widget.htmlAttrs.required == 'true') {
                    html.append(" *")
                }

                html.append("</label>")
                html.append(widget.render())
                html.append("</div>")
            }
        }
        return html
    }

    String renderBeforeForm(String className, Map createWidgetProperties=[:]){
        return _genericRenderMethod("renderBeforeForm", 'create', className, createWidgetProperties)
    }

    String renderAfterForm(String className, Map createWidgetProperties=[:]){
        return _genericRenderMethod("renderAfterForm", 'create', className, createWidgetProperties)
    }

    String _genericRenderMethod(String method, String formType, String className, Map widgetProperties){
        def result = new StringBuilder()

        def domainConfig = adminConfigHolder.getDomainConfig(className)
        List properties = domainConfig.getProperties("create")
        Map customWidgets = domainConfig.getCustomWidgets(formType)

        properties.each { propertyName ->
            def widget = grailsAdminPluginWidgetService.getWidgetForClass(domainConfig.domainClass, propertyName, customWidgets?."$propertyName", widgetProperties)
            def toRender = widget."$method"()

            if (toRender) {
                result << toRender
            }
        }
        return result
    }

    //list

    String renderListLine(Object object){
        def config = adminConfigHolder.getDomainConfig(object)

        List properties = config.getProperties("list")
        StringBuilder html = new StringBuilder()
        properties.each{propertyName ->
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

    String renderListTitle(String className, String sort, String sortOrder){
        def objectClass = this.getClass().classLoader.loadClass(className)
        def object = objectClass?.newInstance()

        def domain = adminConfigHolder.getDomainConfig(object)
        List properties = domain.getProperties("list")
        List sortable = domain.getSortableProperties("list")

        StringBuilder html = new StringBuilder()

        properties.each{ propertyName ->
            def sortLink = ''
            def theClassName = ''

            if (propertyName in sortable) {
                theClassName = 'sortable'
                def order = 'desc'

                if (propertyName == sort) {
                    if (sortOrder == 'asc') {
                        theClassName += " up"
                        order = 'desc'
                    } else {
                        theClassName += " down"
                        order = 'asc'
                    }
                } else {
                    theClassName += " no-sorted"
                }

                sortLink = grailsLinkGenerator.link(mapping: 'grailsAdminList',
                                                    params: ['slug': domain.slug,
                                                             'sort': propertyName,
                                                             'sort_order': order])
            }

            html.append("<th class='${theClassName}'>")
            if (sortLink) {
                html.append("<a href='${sortLink}'>")
            }
            html.append(propertyName)
            html.append("<span></span>")
            if (sortLink) {
                html.append("</a>")
            }
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
        def config = adminConfigHolder.getDomainConfig(object)
        def result = [:]

        if (config) {
            def properties = config.getProperties("list")

            if (object.id) {
                result.id = object.id
            }

            properties.each { propertyName ->
                def val = object."${propertyName}"
                if (val) {
                    def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName)
                    result << ["$propertyName":widget.getValueForJson()]
                } else {
                    result << ["$propertyName":""]
                }
            }
        } else {
            result = object
        }

        result["__text__"] = object.toString()

        return result
    }

    String renderObjectAsJson(Object object) {
        def result = _getInfoForJson(object)
        return new JsonBuilder(result).toString()
    }

    void doWithAssetType(String formType, String className, String type, Closure closure) {
        if (!formType || !className) {
            return
        }
        def domainConfig = adminConfigHolder.getDomainConfig(className)

        if (domainConfig) {
            List properties = domainConfig.getProperties(formType)
            Map customWidgets = domainConfig.getCustomWidgets(formType)

            def builder = new StringBuilder()
            def widgetAssets = []

            properties.each{ propertyName ->
                def widget = grailsAdminPluginWidgetService.getWidgetForClass(domainConfig.domainClass, propertyName, customWidgets?."$propertyName")
                if (widget) {
                    def currentWidgetAssets = widget.assets.findAll { it.endsWith(".$type")}
                    if (currentWidgetAssets) {
                        widgetAssets.addAll(currentWidgetAssets)
                    } else {
                        def slug = widget.class.simpleName.toLowerCase()
                        widgetAssets << "$type/admin/$slug.$type"
                    }
                }
            }
            widgetAssets.unique().each(closure)
        }
    }
}

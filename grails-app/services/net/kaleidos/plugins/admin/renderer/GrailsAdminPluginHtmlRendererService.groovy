package net.kaleidos.plugins.admin.renderer

import groovy.json.JsonBuilder
import net.kaleidos.plugins.admin.DomainInspector

class GrailsAdminPluginHtmlRendererService {
    def adminConfigHolder
    def grailsAdminPluginWidgetService
    def grailsLinkGenerator

    String renderEditFormFields(Object object, Map editWidgetProperties=[:]){
        return _renderFormFields("edit", object, editWidgetProperties)
    }

    String renderCreateFormFields(String className, Map createWidgetProperties=[:]){
        def objectClass = Class.forName(className, true, Thread.currentThread().contextClassLoader)
        def object = objectClass?.newInstance()
        return _renderFormFields("create", object, createWidgetProperties)
    }

    String _renderFormFields(String formType, Object object, Map widgetProperties){
        def writer = new StringWriter()
        def builder = new groovy.xml.MarkupBuilder(writer)

        if (object) {
            def domainConfig = adminConfigHolder.getDomainConfig(object)
            List properties = domainConfig.getDefinedProperties(formType)
            Map customWidgets = domainConfig.getCustomWidgets(formType)


            properties.each{propertyName ->
                def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, customWidgets?."$propertyName", widgetProperties)

                builder.div class:"form-group", {
                    label for:"${propertyName.encodeAsHTML()}", {
                        mkp.yieldUnescaped propertyName.capitalize().encodeAsHTML()
                        if (widget.htmlAttrs.required == 'true') {
                            mkp.yield " *"
                        }
                    }
                    try {
                        mkp.yieldUnescaped widget.render()
                    } catch (Throwable t) {
                        log.error t.message
                        mkp.yieldUnescaped widget.renderError(t)
                    }
                }
            }
        }
        return writer
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
        List properties = domainConfig.getDefinedProperties("create")
        Map customWidgets = domainConfig.getCustomWidgets(formType)

        properties.each { propertyName ->
            def widget = grailsAdminPluginWidgetService.getWidgetForClass(domainConfig.domainClass, propertyName, customWidgets?."$propertyName", widgetProperties)

            def toRender
            try {
                toRender = widget."$method"()
            } catch (Throwable t) {
                log.error t.message
                toRender = widget.renderError()
            }

            if (toRender) {
                result << toRender
            }
        }
        return result
    }

    //list

    String renderListLine(String objClass, Object object){
        def config = adminConfigHolder.getDomainConfig(objClass)

        if (!config) {
            adminConfigHolder.getDomainConfig(object)
        }

        List properties = config.getDefinedProperties("list")
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
        def domain = adminConfigHolder.getDomainConfig(className)
        List properties = domain.getDefinedProperties("list")
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

    void doWithAssetType(String formType, String className, String type, Closure closure) {
        if (!formType || !className) {
            return
        }
        def domainConfig = adminConfigHolder.getDomainConfig(className)

        if (domainConfig) {
            List properties = domainConfig.getDefinedProperties(formType)
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
                        def slug = DomainInspector.getSlug(widget.class)
                        widgetAssets << "$type/admin/$slug.$type"
                    }
                }
            }
            widgetAssets.unique().each(closure)
        }
    }
}

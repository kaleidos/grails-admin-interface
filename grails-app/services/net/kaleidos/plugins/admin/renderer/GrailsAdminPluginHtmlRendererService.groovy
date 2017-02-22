package net.kaleidos.plugins.admin.renderer

import grails.web.mapping.LinkGenerator
import groovy.xml.MarkupBuilder
import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.config.DomainConfig
import net.kaleidos.plugins.admin.widget.GrailsAdminPluginWidgetService
import net.kaleidos.plugins.admin.widget.Widget

class GrailsAdminPluginHtmlRendererService {
    AdminConfigHolder adminConfigHolder
    GrailsAdminPluginWidgetService grailsAdminPluginWidgetService
    LinkGenerator grailsLinkGenerator

    String renderEditFormFields(Object object, Map editWidgetProperties=[:]){
        def domainConfig = adminConfigHolder.getDomainConfig(object)
        return _renderFormFields("edit", domainConfig, object, editWidgetProperties)
    }

    String renderCreateFormFields(String className, Map createWidgetProperties=[:]){
        def domainConfig = adminConfigHolder.getDomainConfig(className)
        return _renderFormFields("create", domainConfig, null, createWidgetProperties)
    }

    String _renderFormFields(String formType, DomainConfig domainConfig, Object object, Map widgetProperties){
        Writer writer = new StringWriter()
        MarkupBuilder builder = new groovy.xml.MarkupBuilder(writer)

        Map customWidgets = domainConfig.getCustomWidgets(formType)
        List properties = domainConfig.getDefinedPropertiesForGroup(formType)
        if (properties) {
            _renderProperties(object, properties, customWidgets, domainConfig, widgetProperties, builder)
        }

        domainConfig.groupNames.each { groupName ->
            properties = domainConfig.getDefinedPropertiesForGroup(formType, groupName)
            def style = domainConfig.getStylePropertiesForGroup(groupName)

            if (style == "collapse") {
                def groupSlug = groupName.replaceAll(" ", "_")

                builder.div class:"panel panel-default",  {
                    div class:"panel-heading", {
                        h3 class:"panel-title", {
                            a "data-toggle":"collapse", "data-target":"#${groupSlug}Panel", "href":"#${groupSlug}Panel", class: "collapsed", {
                                mkp.yield groupName
                            }
                        }
                    }
                    div id:"${groupSlug}Panel", class:"panel-collapse collapse", {
                        div class:"panel-body", {
                            _renderProperties(object,properties, customWidgets, domainConfig, widgetProperties, builder)
                        }
                    }
                }
            } else {
                builder.div class:"panel panel-default",  {
                    div class:"panel-heading", {
                        h3 class:"panel-title", {
                            mkp.yield groupName
                        }
                    }
                    div class:"panel-body", {
                        _renderProperties(object,properties, customWidgets, domainConfig, widgetProperties, builder)
                    }
                }
            }
        }
        return writer
    }

    void _renderProperties(object, properties, customWidgets, domainConfig, widgetProperties, builder) {
        properties.each { propertyName ->
            Widget widget
            if (object != null) {
                widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, customWidgets?."$propertyName", widgetProperties)
            } else {
                widget = grailsAdminPluginWidgetService.getWidgetForClass(domainConfig.domainClass, propertyName, customWidgets?."$propertyName", widgetProperties)
            }

            builder.div class:"form-group", {
                label for:"${propertyName.encodeAsHTML()}", {
                    mkp.yieldUnescaped propertyName.capitalize().encodeAsHTML()
                    if (widget.htmlAttrs.required == 'true') {
                        mkp.yield " *"
                    }
                    if (widget.internalAttrs.help) {
                        span(class:"glyphicon glyphicon-question-sign", title:"${widget.internalAttrs.help}")
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

    String renderBeforeForm(String className, Map createWidgetProperties=[:]){
        return _genericRenderMethod("renderBeforeForm", 'create', className, createWidgetProperties)
    }

    String renderAfterForm(String className, Map createWidgetProperties=[:]){
        return _genericRenderMethod("renderAfterForm", 'create', className, createWidgetProperties)
    }

    String _genericRenderMethod(String method, String formType, String className, Map widgetProperties){
        StringBuilder result = new StringBuilder()

        DomainConfig domainConfig = adminConfigHolder.getDomainConfig(className)
        List properties = domainConfig.getDefinedProperties("create")
        Map customWidgets = domainConfig.getCustomWidgets(formType)

        properties.each {String propertyName ->
            Widget widget = grailsAdminPluginWidgetService.getWidgetForClass(domainConfig.domainClass, propertyName, customWidgets?."$propertyName", widgetProperties)

            String toRender
            try {
                toRender = widget."$method"()
            } catch (Throwable t) {
                log.error t.message
                toRender = widget.renderError(t)
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
                if (isCollectionOrArray(val)){
                    html.append(val.join(", "))
                } else if (val && val.respondsTo("encodeAsHTML")) {
                    html.append(val.encodeAsHTML())
                } else {
                    html.append(val?:'&nbsp;')
                }
            }


            // TODO: Who has to decide how to encode? Widget or this method?


            html.append("</td>")
        }
        return html
    }

    boolean isCollectionOrArray(object) {
        [Collection, Object[]].any { it.isAssignableFrom(object.getClass()) }
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

        DomainConfig domainConfig = adminConfigHolder.getDomainConfig(className)

        if (domainConfig) {
            List properties = domainConfig.getDefinedProperties(formType)
            Map customWidgets = domainConfig.getCustomWidgets(formType)

            List widgetAssets = []

            properties.each{ propertyName ->
                def widget = grailsAdminPluginWidgetService.getWidgetForClass(domainConfig.domainClass, propertyName, customWidgets?."$propertyName")
                if (widget) {
                    def currentWidgetAssets = widget.assets.findAll { it.file.endsWith(".$type")}
                    if (currentWidgetAssets) {
                        widgetAssets.addAll(currentWidgetAssets)
                    }
                }
            }
            widgetAssets.unique().each(closure)
        }
    }
}

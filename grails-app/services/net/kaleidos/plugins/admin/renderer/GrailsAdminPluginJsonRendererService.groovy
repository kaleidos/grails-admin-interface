package net.kaleidos.plugins.admin.renderer

import groovy.json.JsonBuilder
import net.kaleidos.plugins.admin.DomainInspector

class GrailsAdminPluginJsonRendererService {
    def adminConfigHolder
    def grailsAdminPluginWidgetService

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
            def properties = config.getDefinedProperties("list")

            if (object.id) {
                if (object.id instanceof Long) {
                    result.id = object.id
                } else {
                    result.id = "${object.id}"
                }
            }

            properties.each { propertyName ->
                def val = object."${propertyName}"
                if (val) {
                    def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName)
                    result << ["$propertyName":widget.getValueForJson()]
                } else {
                    result << ["$propertyName": val]
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
}

package net.kaleidos.plugins.admin

import net.kaleidos.plugins.admin.config.DomainConfig

class GrailsAdminPluginDataService {
    static transactional = true

    def grailsApplication
    def grailsAdminPluginWidgetService
    def adminConfigHolder

    List listDomain(Class domainClass){
        return domainClass.list()
    }

    def retrieveDomain(Class domainClass, Long id){
        return domainClass.get(id)
    }

    def saveDomain(Class domainClass, Map params){
        def domainObj = domainClass.newInstance()
        def domainConfig = adminConfigHolder.getDomainConfig(domainClass)
        if (!domainConfig) {
            throw new RuntimeException("Domain not configured for ${domainClass.name}")
        }
        return _saveObject(domainConfig, domainObj, "create", params)
    }

    def updateDomain(Class domainClass, Long id, Map params){
        def domainObj = domainClass.get(id)
        def domainConfig = adminConfigHolder.getDomainConfig(domainClass)
        if (!domainObj) {
            throw new RuntimeException("Object with id $id doesn't exist")
        }
        return _saveObject(domainConfig, domainObj, "edit", params)
    }

    def list(Class domainClass, Long offset = 0, Long limit = 10, String sort = 'id', String order = 'asc') {
        return domainClass.list(offset: offset,
                                max: limit,
                                sort: sort,
                                order: order)
    }

    def count(Class domainClass) {
        return domainClass.count()
    }

    Boolean deleteDomain(Class domainClass, Long objectId){
        def result = false
        def domainObj = domainClass.get(objectId)
        if (domainObj) {
            domainObj.delete()
            result = true
        }
        return result
    }


    void deleteRelatedDomain(Class domainClass, Long objectId, String propertyName, Long objectId2){
        def domainObj = domainClass.get(objectId)
        if (domainObj) {
            def inspector = new DomainInspector(domainClass)
            if (inspector.isOneToMany(propertyName)){
                def cap = propertyName.capitalize()
                def element = domainObj."$propertyName".find { it.id = objectId2 }
                domainObj."removeFrom$cap"(element)
            }
        }
    }

    void putRelatedDomain(Class domainClass, Long objectId, String propertyName, Long objectId2){
        def domainObj = domainClass.get(objectId)
        if (domainObj) {
            def inspector = new DomainInspector(domainClass)
            if (inspector.isOneToMany(propertyName)){
                def element = property.getReferencedDomainClass().clazz.get(objectId2)
                if (element) {
                    def cap = propertyName.capitalize()
                    domainObj."addTo$cap"(element)
                }
            }
        }
    }

    def _saveObject(DomainConfig domainConfig, Object object, String method, Map params) {
        List properties = domainConfig.getDefinedProperties(method)
        Map customWidgets = domainConfig.getCustomWidgets(method)?:[:]

        def errors
        properties.each{key ->
            try {
                _setValue(object, customWidgets[key], key, params["$key"])
            } catch (Throwable t) {
                if (!errors) {
                    errors = new grails.validation.ValidationErrors(object)
                }
                errors.rejectValue(key, params["$key"], t.message)
            }
        }
        // Need to throw validation exception
        if(!object.save()) {
            if (!errors) {
                errors = object.errors
            } else {
                errors.addAllErrors(object.errors)
            }
        }

        if (errors) {
            throw new grails.validation.ValidationException("Couldn't update ${domainConfig.classFullName}", errors)
        }

        return object
    }

    def _setValue(def object, def customWidget, def propertyName, def val){
        def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, customWidget)
        widget.value = val
        widget.updateValue()
    }
}

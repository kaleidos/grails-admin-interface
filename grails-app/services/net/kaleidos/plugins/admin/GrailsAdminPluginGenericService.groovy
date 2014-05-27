package net.kaleidos.plugins.admin

class GrailsAdminPluginGenericService {
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

        List properties = domainConfig.getDefinedProperties("create")
        Map customWidgets = domainConfig.getCustomWidgets("create")?:[:]

        properties.each{key ->
            _setValue(domainObj, customWidgets[key], key, params["$key"])
        }
        domainObj.save(failOnError:true)
        return domainObj
    }

    def updateDomain(Class domainClass, Long id, Map params){
        def result = domainClass.get(id)

        if (!result) {
            throw new RuntimeException("Object with id $id doesn't exist")
        }
        def domainConfig = adminConfigHolder.getDomainConfig(domainClass)
        List properties = domainConfig.getDefinedProperties("edit")
        Map customWidgets = domainConfig.getCustomWidgets("edit")?:[:]
        properties.each{key ->
            _setValue(result, customWidgets[key], key, params["$key"])
        }
        // Need to throw validation exception
        result.save(failOnError:true)
        return result
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

    def _setValue(def object, def customWidget, def propertyName, def val){
        def widget = grailsAdminPluginWidgetService.getWidget(object, propertyName, customWidget)
        widget.value = val?:null
        widget.updateValue()
    }
}

package net.kaleidos.plugins.admin

import grails.transaction.Transactional

@Transactional
class GrailsAdminPluginGenericService {
    def grailsApplication
    def grailsAdminPluginWidgetService

    List listDomains(Class<?> domainClass){
        return domainClass.list()
    }

    def retrieveDomain(Class<?> domainClass, Long id){
        return domainClass.get(id)
    }

    def saveDomain(Class<?> domainClass, Map params){
        def domainObj = domainClass.newInstance()
        domainObj.properties = params
        domainObj.save(failOnError:true)
        return domainObj
    }

    def updateDomain(Class<?> domainClass, Long id, Map params){
        def result = domainClass.get(id)

        if (!result) {
            throw new RuntimeException("Object with id $id doesn't exist")
        }

        params.each { key, val ->
            if ((!(key in ["id", "version"])) && result.hasProperty(key)) {
                result."$key" = _getValueByType(domainClass, key, val)
            }
        }

        // Need to throw validation exception
        result.save(failOnError:true)

        return result
    }

    def list(Class<?> domainClass, Long offset = 0, Long limit = 10) {
        return domainClass.list(offset: offset, max: limit, sort: 'id', order: 'asc')
    }

    def count(Class<?> domainClass) {
        return domainClass.count()
    }

    Boolean deleteDomain(Class<?> domainClass, Long objectId){
        def result = false
        def domainObj = domainClass.get(objectId)
        if (domainObj) {
            domainObj.delete()
            result = true
        }
        return result
    }

    def _getValueByType(def domainClass, def propertyName, def val){
        if (val) {
            def property = grailsAdminPluginWidgetService.getGrailsDomainClass(domainClass).getPersistentProperty(propertyName)
            switch ( property.type ) {
                case Date:
                    return Date.parse("MM/dd/yyyy", testDate)
                    break
                default:
                    if (grailsApplication.isDomainClass(property.type)){
                        return retrieveDomain(property.type, val as Long)
                    }
                    return val

            }
            return val
        }
        return null
    }
}

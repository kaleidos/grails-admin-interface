package net.kaleidos.plugins.admin

import grails.transaction.Transactional

@Transactional
class GrailsAdminPluginGenericService {
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
                result."$key" = val
            }
        }

        result.save(failOnError:true)

        return result
    }

    def list(Class<?> domainClass, Long offset = 0, Long limit = 10) {
        return domainClass.list(offset: offset, max: limit, sort: 'id', order: 'asc')
    }

    def count(Class<?> domainClass) {
        return domainClass.count()
    }

    void deleteDomain(Class<?> domainClass, Long objectId){
        def domainObj = domainClass.get(objectId)
        domainObj.delete()
    }
}

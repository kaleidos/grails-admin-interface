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
        if (!domainObj.save()) {
            throw new RuntimeException("Couldn't save the domain object: ${domainObj.errors}")
        }
        return domainObj
    }

    def updateDomain(Class<?> domainClass, Long id, Map params){
        def result = domainClass.get(id)

        if (!result) {
            throw new RuntimeException("Object with id $id doesn't exist")
        }

        params.each { key, val ->
            result."$key" = val
        }

        if (!result.save()) {
            throw new RuntimeException("Couldn't save the domain object: ${result.errors}")
        }

        return result
    }

    void deleteDomain(Class<?> domainClass, Long objectId){
        def domainObj = domainClass.get(objectId)
        domainObj.delete()
    }
}

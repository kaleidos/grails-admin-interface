package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.GrailsDomainClass

class DomainConfig {
    GrailsDomainClass domainClass
    Map excludes = [:]
    Map includes = [:]

    public DomainConfig(GrailsDomainClass domainClass, Map params) {
        this.domainClass = domainClass

        if (params) {
            _configureParams(params)
            
            if (params['adminClass']) {
                def clazz = Class.forName(params['adminClass'])
                if (clazz.metaClass.respondsTo(clazz, "getOptions")) {
                    _configureParams(clazz.options)
                }
            }
        }
    }

    List getExcludes(String method) {
        return this.excludes[method]
    }

    List getIncludes(String method) {
        return this.includes[method]
    }
    
    private _configureParams(params) {
        params.each { method, properties ->
            if (['list', 'show', 'edit'].contains(method)) {
                if (properties['excludes']) {
                    this.excludes[method] = properties['excludes']
                }

                if (properties['includes']) {
                    this.includes[method] = properties['includes']
                }
            }
        }
    }
}

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

    List getProperties(String method) {
        def result = domainClass.getProperties()
            .findAll{ it.isPersistent() }
            .collect { it.name }

        if (includes[method]) {
            result = result.findAll{ includes[method].contains(it) }
        } else if (excludes[method]) {
            result = result.findAll{ !excludes[method].contains(it) }
        }
        return result.sort()
    }

    List getExcludes(String method) {
        return this.excludes[method]
    }

    List getIncludes(String method) {
        return this.includes[method]
    }

    private _configureParams(params) {
        params.each { method, properties ->
            if (['list', 'create', 'edit'].contains(method)) {
                if (properties['excludes'] && properties['includes']) {
                    throw new RuntimeException("The includes and exludes configuration is setted for domain: ${domainClass.name}. Only should be defined")
                }

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

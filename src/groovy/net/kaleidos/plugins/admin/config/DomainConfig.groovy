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
        }
    }

    List getProperties(String method) {
        def defaultExclude = ['id','version']
        def result = domainClass.getProperties()
            .findAll{ it.isPersistent() }
            .collect { it.name }
            .sort()

        if (includes[method]) {
            result = includes[method].findAll { result.contains(it) }
        } else if (excludes[method]) {
            result = result.findAll{ !excludes[method].contains(it) && !defaultExclude.contains(it) }
        } else {
            result = result.findAll{ !defaultExclude.contains(it) }
        }
        return result
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
                    throw new RuntimeException("The includes and exludes configuration is setted for domain: ${domainClass.name}. Only one can be defined")
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

    public String getClassName() {
        return this.domainClass.name
    }

    public String getClassFullName() {
        return this.domainClass.fullName
    }

    public String getSlug() {
        return this.domainClass.name.toLowerCase()
    }
}

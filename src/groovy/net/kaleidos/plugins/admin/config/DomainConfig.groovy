package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.GrailsDomainClass
import java.util.concurrent.ConcurrentHashMap

class DomainConfig {
    private static final ConcurrentHashMap<String,Boolean> transientPropertiesCache = new ConcurrentHashMap()
    GrailsDomainClass domainClass
    Map excludes = [:]
    Map includes = [:]
    Map customWidgets = [:]

    public DomainConfig(GrailsDomainClass domainClass, Map params) {
        this.domainClass = domainClass
        if (params) {
            _configureParams(params)
        }
    }

    List getProperties(String method) {
        def defaultExclude = ['id','version']

        def result = domainClass.getPersistentProperties().collect { it.name }

        if (includes[method]) {
            result = includes[method].findAll { result.contains(it) }
        } else if (excludes[method]) {
            result = result.findAll{ !excludes[method].contains(it) && !defaultExclude.contains(it) }
        } else {
            result = result.findAll{ !defaultExclude.contains(it) }
        }
        return result
    }

    List getSortableProperties(String method) {
        def properties = getProperties(method)

        return properties.findAll {
            return !domainClass.getPropertyByName(it).isAssociation()
        }
    }


    List getExcludes(String method) {
        return this.excludes[method]
    }

    List getIncludes(String method) {
        return this.includes[method]
    }

    Map getCustomWidgets(String method) {
        return this.customWidgets[method]
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

                if (properties['customWidgets']) {
                    this.customWidgets[method] = properties['customWidgets']
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

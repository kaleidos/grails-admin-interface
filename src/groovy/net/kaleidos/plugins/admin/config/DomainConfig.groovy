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

    private _isTransient(Class clazz, String property) {
        def field = null
        def curClazz = clazz

        String key = "${clazz}_${property}"

        if (!transientPropertiesCache.containsKey(key)) {
            while (curClazz && !field) {
                try {
                    field = curClazz.getDeclaredField(property)
                } catch(java.lang.NoSuchFieldException e) {
                    curClazz = curClazz.getSuperclass()
                }
            }
            if (field) {
                transientPropertiesCache.putIfAbsent(key, java.lang.reflect.Modifier.isTransient(field.modifiers))
            } else {
                transientPropertiesCache.putIfAbsent(key, false)
            }
        }
        return transientPropertiesCache.get(key)

    }

    List getProperties(String method) {
        def defaultExclude = ['id','version']

        def result = domainClass.getProperties()
            .findAll{ it.isPersistent() && !_isTransient(domainClass.clazz, it.name)}
            .collect { it.name }

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

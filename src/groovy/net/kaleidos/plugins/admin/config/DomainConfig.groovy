package net.kaleidos.plugins.admin.config

import net.kaleidos.plugins.admin.DomainInspector
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import java.util.concurrent.ConcurrentHashMap

class DomainConfig {
    Class domainClass
    DomainInspector domainInspector

    Map excludes = [:]
    Map includes = [:]
    Map customWidgets = [:]

    public DomainConfig(Class domainClass) {
        this.domainClass = domainClass
        this.domainInspector = new DomainInspector(domainClass)
    }

    List getDefinedProperties(String method) {
        def defaultExclude = []

        def result = this.domainInspector.getPropertyNames()

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
        def properties = getDefinedProperties(method)
        return properties.findAll(this.domainInspector.&isSortable)
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

    public String getClassName() {
        return this.domainClass.name.tokenize(".")[-1]
    }

    public String getClassFullName() {
        return this.domainClass.name
    }

    public String getSlug() {
        return this.domainInspector.getSlug()
    }
}

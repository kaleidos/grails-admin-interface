package net.kaleidos.plugins.admin.config

import net.kaleidos.plugins.admin.DomainInspector

class DomainConfig {
    Class domainClass
    DomainInspector domainInspector

    Map excludes = [:]
    Map includes = [:]
    Map customWidgets = [:]
    Map fieldGroups = [:]

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

    public List getGroupNames() {
        return fieldGroups.keySet() as List
    }

    public List getDefinedPropertiesForGroup(String method, String groupName=null) {
        def properties = this.getDefinedProperties(method)
        if (!groupName) {
            def propertiesWithGroup = fieldGroups.collect{ it.value.fields }.flatten()
            return properties - propertiesWithGroup
        }
        return fieldGroups[groupName]?.fields
    }
    public String getStylePropertiesForGroup(String groupName=null){
        return fieldGroups[groupName]?.style
    }
}

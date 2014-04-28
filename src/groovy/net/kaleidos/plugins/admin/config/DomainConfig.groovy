package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.GrailsDomainClass

class DomainConfig {
    GrailsDomainClass domainClass
    Map excludes = [:]
    Map includes = [:]

    public DomainConfig(GrailsDomainClass domainClass, Map params) {
        this.domainClass = domainClass

        if (params) {
            params.each { method, properties ->
                println "$method, $properties"

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

    List getExcludes(String method) {
        return this.excludes[method]
    }

    List getIncludes(String method) {
        return this.includes[method]
    }
}

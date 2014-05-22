package net.kaleidos.plugins.admin.config

import groovy.util.logging.Log4j
import java.util.regex.Pattern
import org.codehaus.groovy.grails.web.mapping.RegexUrlMapping
import org.springframework.beans.factory.NoSuchBeanDefinitionException

import org.codehaus.groovy.grails.web.mapping.*
import grails.util.Holders

import org.springframework.web.context.WebApplicationContext
import org.springframework.util.ClassUtils

import org.codehaus.groovy.grails.validation.ConstrainedProperty;
import org.codehaus.groovy.grails.web.mapping.DefaultUrlMappingsHolder

import net.kaleidos.plugins.admin.widget.Widget
import net.kaleidos.plugins.admin.DomainInspector

@Log4j
class AdminConfigHolder {
    Map<String, DomainConfig> domains = [:]

    void initialize() {
        def domainList = Holders.config.grails.plugin.admin.domains
        if (!domainList) {
            return;
        }
        log.debug "Configuring domain classes"

        this.domains = [:]
        domainList.each { name ->
            def inspector = DomainInspector.find(name)

            if (!inspector) {
                throw new RuntimeException("Configured class ${name} is not a domain class")
            }

            def domainConfig = Holders.config.grails.plugin.admin.domain."${inspector.className}"

            if (domainConfig && domainConfig instanceof Closure) {
                def dsl = new DomainConfigurationDsl(inspector.clazz, domainConfig)
                domains[name] = dsl.execute()
            } else if (domainConfig && domainConfig instanceof String) {
                def clazz = Class.forName(domainConfig)
                if (!clazz.metaClass.respondsTo(clazz, "getOptions")) {
                    throw new RuntimeException("Class $domainConfig doesn't have a static attribute 'options'")
                }
                def dsl = new DomainConfigurationDsl(inspector.clazz, clazz.options)
                domains[name] = dsl.execute()
            } else {
                domains[name] = new DomainConfig(inspector.clazz)
            }
        }
        log.debug "DOMAIN: ${this.domains}"
    }

    public List getDomainClasses() {
        return this.domains.keySet() as List
    }

    public List<String> getDomainNames() {
        return this.domainClasses.collect{ this.domains[it].className }
    }

    public List<String> getSlugDomainNames() {
        return this.domainClasses.collect{ this.domains[it].slug }
    }

    public DomainConfig getDomainConfig(String objClass) {
        try {
            return getDomainConfig(Class.forName(objClass, true, Thread.currentThread().contextClassLoader))
        } catch (ClassNotFoundException e) {
            // Sometimes Domain classes throws a ClassNotFoundException. We shoudl fall-back to the grails implementation
            return Holders.grailsApplication.getClassForName(objClass)
        }
    }

    public DomainConfig getDomainConfig(Object object) {
        if (!object) {
            return null
        }
        def clazz = ClassUtils.getUserClass(object?.getClass())
        return getDomainConfig(clazz)
    }

    public DomainConfig getDomainConfig(Class objClass) {
        if (!objClass || Object.class == objClass) {
            return null
        }
        def config = this.domains[objClass.name]

        if (!config) {
            config = getDomainConfig(objClass.getSuperclass())
        }

        return config
    }

    public DomainConfig getDomainConfigForProperty(Object object, String property) {
        def clazz = ClassUtils.getUserClass(object.getClass())
        return getDomainConfigForProperty(clazz, property)
    }

    public DomainConfig getDomainConfigForProperty(Class objClass, String property) {
        def inspector = new DomainInspector(objClass)
        return getDomainConfig(inspector.getPropertyClass(property))
    }

    public DomainConfig getDomainConfigBySlug(String slug) {
        return this.domains.find { it.value.slug == slug }?.value
    }
}

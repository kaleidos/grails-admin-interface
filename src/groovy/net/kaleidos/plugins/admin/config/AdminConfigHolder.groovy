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

@Log4j
class AdminConfigHolder {
    def grailsApplication

    Map<String, DomainConfig> domains = [:]
    String accessRoot = "admin"
    String role = "ROLE_ADMIN"

    ConfigObject config
    List domainList = null

    Set widgetsClasses

    public AdminConfigHolder(ConfigObject config=null) {
        if (config != null) {
            this.config =  config

            if (config.grails.plugin.admin.domains) {
                this.domainList = config.grails.plugin.admin.domains
            }

            if (config.grails.plugin.admin.access_root) {
                this.accessRoot = "${config.grails.plugin.admin.access_root}"

                // Remove the starting slash for the access root
                if (this.accessRoot.startsWith("/")) {
                    this.accessRoot = this.accessRoot.substring(1)
                }
            }

            if (config.grails.plugin.admin.role) {
                this.role = "${config.grails.plugin.admin.role}"
            }

        }
    }

    void initialize() {
        _configureAdminRole()
        _configureDomainClasses()

        log.debug "Loading config. Domains: ${this.domains}"
        log.debug "Loading config. Access Root: ${this.accessRoot}"
        log.debug "Loading config. Role: ${this.role}"
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
            return grailsApplication.getClassForName(objClass)
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
        def field = objClass.getDeclaredFields().find { it.name == property }

        if (!field && objClass.getSuperclass() && objClass != Object.class) {
            // check superclass
            return getDomainConfigForProperty(objClass.getSuperclass(), property)
        }

        def propertyClass = field?.type
        return getDomainConfig(propertyClass)
    }

    public DomainConfig getDomainConfigBySlug(String slug) {
        return this.domains.find { it.value.slug == slug }?.value
    }

    List<String> getViewResources(String type){
        def result = []
        if (type == "css") {
            result << 'grails-admin/libs/bootstrap/css/bootstrap.css'
            result << 'grails-admin/libs/bootstrap/css/bootstrap-theme.css'
            result << 'grails-admin/css/main.css'
        }

        if (type == "js") {
            result << 'grails-admin/libs/jquery/jquery.js'
            result << 'grails-admin/libs/bootstrap/js/bootstrap.js'
            result << 'grails-admin/libs/injectorJS/injector.js'
            result << 'grails-admin/libs/parsleyjs/parsley.remote.js'
            result << 'grails-admin/libs/serializeObject.js'
            result << 'grails-admin/js/main.js'
            result << 'grails-admin/js/views/formView.js'
            result << 'grails-admin/js/views/deleteModalView.js'
            result << 'grails-admin/js/general.js'
        }

        return result
    }

    private _configureAdminRole() {
        try {
            // try to get the Secured annotation of Spring security 2
            Class.forName("grails.plugin.springsecurity.annotation.Secured")
            _configureAdminRoleSecurity2()
        } catch (Throwable e) {
            // If it fails we configure for spring security core 1.x
            _configureAdminRoleSecurity1()
        }
    }

    private void _configureAdminRoleSecurity1() {
        try {
            def clazz =  Class.forName("org.springframework.security.access.SecurityConfig")
            def constructor = clazz.getConstructor(String.class)
            def newConfig = constructor.&newInstance

            def objectDefinitionSource = grailsApplication.mainContext.getBean("objectDefinitionSource")
            objectDefinitionSource.storeMapping("/grailsadminplugin/**", [newConfig(this.role)] as Set)
            objectDefinitionSource.storeMapping("/grailsadminpluginapi/**", [newConfig(this.role)] as Set)
            objectDefinitionSource.storeMapping("/grailsadminplugincallbackapi/**", [newConfig(this.role)] as Set)
        } catch (NoSuchBeanDefinitionException e) {
            log.error "No configured Spring Security"
        } catch (ClassNotFoundException e) {
            log.error "No configured Spring Security"
        }
    }

    private void _configureAdminRoleSecurity2() {
        try {
            // We use reflection so it doesn't have a compile-time dependency
            def clazz = Class.forName("grails.plugin.springsecurity.InterceptedUrl")
            def httpMethodClass = Class.forName("org.springframework.http.HttpMethod")
            def constructor = clazz.getConstructor(String.class, Collection.class, httpMethodClass)
            def newUrl = constructor.&newInstance

            def objectDefinitionSource = grailsApplication.mainContext.getBean("objectDefinitionSource")
            objectDefinitionSource.compiled << newUrl("/grailsadminplugin", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugin.*", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugin/**", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginapi", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginapi.*", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginapi/**", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugincallbackapi", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugincallbackapi.*", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugincallbackapi/**", [this.role], null)
        } catch (NoSuchBeanDefinitionException e) {
            log.error "No configured Spring Security"
        } catch (ClassNotFoundException e) {
            log.error "No configured Spring Security"
        }
    }

    private _configureDomainClasses(){
        if (!this.domainList) {
            return;
        }
        log.debug "Configuring domain classes"

        this.domains = [:]
        this.domainList.each { name ->
            def domainClass = grailsApplication.domainClasses.find { it.fullName == name }

            if (!domainClass) {
                throw new RuntimeException("Configured class ${name} is not a domain class")
            }

            def className = name.tokenize(".")[-1]
            def domainConfig = config.grails.plugin.admin.domain."$className"

            if (domainConfig && domainConfig instanceof Closure) {
                def dsl = new DomainConfigurationDsl(domainConfig)
                dsl.grailsApplication = this.grailsApplication
                def params = dsl.execute()
                domains[name] = new DomainConfig(domainClass, params)
            } else if (domainConfig && domainConfig instanceof String) {
                def clazz = Class.forName(domainConfig)
                if (!clazz.metaClass.respondsTo(clazz, "getOptions")) {
                    throw new RuntimeException("Class $domainConfig doesn't have a static attribute 'options'")
                }
                def dsl = new DomainConfigurationDsl(clazz.options)
                dsl.grailsApplication = this.grailsApplication
                def params = dsl.execute()
                domains[name] = new DomainConfig(domainClass, params)
            } else {
                domains[name] = new DomainConfig(domainClass, null)
            }
        }
        log.debug "DOMAIN: ${this.domains}"
    }


}

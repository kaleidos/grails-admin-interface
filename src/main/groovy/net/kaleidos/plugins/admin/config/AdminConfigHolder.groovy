package net.kaleidos.plugins.admin.config

import grails.util.Environment
import grails.util.Holders
import groovy.util.logging.Log4j
import net.kaleidos.plugins.admin.DomainInspector
import org.springframework.util.ClassUtils

@Log4j
class AdminConfigHolder {
    private static final String DEFAULT_GROUP = "Domains"

    Map<String, DomainConfig> domains = [:]
    Map<String, List<DomainConfig>> domainGroups = [:]

    void initialize() {
        _mergeConfiguration()
        _configureAdminRole()
        _configureDomains()
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
        return getDomainConfig(Holders.grailsApplication.getClassForName(objClass))
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
            config = _getParentDomainConfig(objClass)
        }

        if (!config && DomainInspector.isDomain(objClass) && Holders.config.grails.plugin.admin.allowDefaultConfig) {
            config = new DomainConfig(objClass)
        }

        return config
    }

    public DomainConfig _getParentDomainConfig(Class clazz) {
        def objClass = clazz.getSuperclass()
        if (!objClass || Object.class == objClass) {
            return null
        }

        def config = this.domains[objClass.name]
        if (!config) {
            config = _getParentDomainConfig(objClass)
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
        def config = this.domains.find { it.value.slug == slug }?.value

        if (!config) {
            def clazz = DomainInspector.getClassWithSlug(slug)
            if (clazz) {
                config = new DomainConfig(clazz)
            }
        }

        return config
    }

    public List getGroups() {
        if (this.domainGroups.isEmpty()) {
            return [DEFAULT_GROUP]
        }
        return this.domainGroups.keySet() as List
    }

    public List getGroup(String groupName=null) {
        if (!groupName || groupName == "" || groupName == DEFAULT_GROUP) {
            return this.domains.values() as List
        }
        return this.domainGroups[groupName]
    }

    def _mergeConfiguration() {
        def userConfiguration = Holders.config.grails.plugin.admin
        def configSlurper = new ConfigSlurper(Environment.getCurrent().getName())
        def defaultConfiguration = configSlurper.parse(Holders.grailsApplication.classLoader.loadClass("GrailsAdminDefaultConfig"))
        Holders.config.grails.admin = _mergeConfigObjects(defaultConfiguration.defaultAdminConfig, userConfiguration)
    }

    def _mergeConfigObjects(ConfigObject confDefault, ConfigObject confUser) {
        def config = new ConfigObject()
        if (confUser == null) {
            config.putAll(confDefault)
        }
        else {
            config.putAll(confDefault)
            config.putAll(confDefault.merge(confUser))
        }
        return config
    }

    def _configureDomains() {
        def domainList = Holders.config.grails.plugin.admin.domains
        if (!domainList) {
            return;
        }
        log.debug "Configuring domain classes"

        this.domains = [:]
        this.domainGroups = [:]

        domainList.each { element ->
            if (element instanceof String) {
                def name = element as String
                domains[name] = _processsDomainConfig(name)
            } else { // It's a nested map
                def groupName = element.key as String
                def groupDomainsList = element.value as List

                groupDomainsList.each { name ->
                    def domainConfig = _processsDomainConfig(name)
                    domains[name] = domainConfig
                    if (!this.domainGroups[groupName]) {
                        this.domainGroups[groupName] = []
                    }
                    this.domainGroups[groupName].add(domainConfig)
                }
            }
        }
        log.debug "DOMAIN: ${this.domains}"
    }

    def DomainConfig _processsDomainConfig(String name) {
        def inspector = DomainInspector.find(name)

        if (!inspector) {
            throw new RuntimeException("Configured class ${name} is not a domain class")
        }

        def domainConfig = Holders.config.grails.plugin.admin.domain."${inspector.className}"

        if (domainConfig && domainConfig instanceof Closure) {
            def dsl = new DomainConfigurationDsl(inspector.clazz, domainConfig)
            return dsl.execute()
        } else if (domainConfig && domainConfig instanceof String) {
            def clazz = Class.forName(domainConfig, true, Thread.currentThread().contextClassLoader)
            if (!clazz.metaClass.respondsTo(clazz, "getOptions")) {
                throw new RuntimeException("Class $domainConfig doesn't have a static attribute 'options'")
            }
            def dsl = new DomainConfigurationDsl(inspector.clazz, clazz.options)
            return dsl.execute()
        } else {
            return new DomainConfig(inspector.clazz)
        }
    }

    def _configureAdminRole() {
        if (!_configureAdminRoleSecurity1() && !_configureAdminRoleSecurity2()) {
            log.error "No configured Spring Security"
            if (Environment.current == Environment.PRODUCTION && Holders.config.grails.plugin.admin.security.forbidUnsecureProduction) {
                String message = "You have not configured Spring Security. You can deactivate this feature setting 'grails.plugin.admin.security.forbidUnsecureProduction=false' in your configuration file"
                log.error message
                throw new RuntimeException(message)
            }
        }
    }

    boolean _configureAdminRoleSecurity1() {
        try {
            def role = Holders.config.grails.plugin.admin.security.role?:"ROLE_ADMIN"

            def clazz =  Class.forName("org.springframework.security.access.SecurityConfig")
            def constructor = clazz.getConstructor(String.class)
            def newConfig = constructor.&newInstance

            def objectDefinitionSource = Holders.grailsApplication.mainContext.getBean("objectDefinitionSource")
            objectDefinitionSource.storeMapping("/grailsadminpluginui/**", [newConfig(role)] as Set)
            objectDefinitionSource.storeMapping("/grailsadminpluginapi/**", [newConfig(role)] as Set)
            objectDefinitionSource.storeMapping("/grailsadminplugincallbackapi/**", [newConfig(role)] as Set)
        } catch (Throwable e) {
            return false
        }
    }

    boolean _configureAdminRoleSecurity2() {
        try {
            def role = Holders.config.grails.plugin.admin.security.role?:"ROLE_ADMIN"

            // We use reflection so it doesn't have a compile-time dependency
            def clazz = Class.forName("grails.plugin.springsecurity.InterceptedUrl")
            def httpMethodClass = Class.forName("org.springframework.http.HttpMethod")
            def constructor = clazz.getConstructor(String.class, Collection.class, httpMethodClass)
            def newUrl = constructor.&newInstance

            def objectDefinitionSource = Holders.grailsApplication.mainContext.getBean("objectDefinitionSource")
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginui", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginui.*", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginui/**", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginapi", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginapi.*", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginapi/**", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugincallbackapi", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugincallbackapi.*", [role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugincallbackapi/**", [role], null)
        } catch (Throwable e) {
            return false
        }
    }
}

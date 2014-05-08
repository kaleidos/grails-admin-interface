package net.kaleidos.plugins.admin.config

import groovy.util.logging.Log4j
import java.util.regex.Pattern
import org.codehaus.groovy.grails.web.mapping.RegexUrlMapping
import org.springframework.beans.factory.NoSuchBeanDefinitionException

import org.codehaus.groovy.grails.web.mapping.*
import grails.util.Holders

import org.springframework.web.context.WebApplicationContext

@Log4j
class AdminConfigHolder {
    def grailsApplication

    Map<String, DomainConfig> domains = [:]
    String accessRoot = "admin"
    String role = "ROLE_ADMIN"

    ConfigObject config
    List domainList = null

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
        _configureUrlMappings()
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

    public DomainConfig getDomainConfig(Object object) {
        return this.domains[object.class.name]
    }

    public DomainConfig getDomainConfig(Class objClass) {
        return this.domains[objClass.name]
    }

    public DomainConfig getDomainConfigBySlug(String slug) {
        return this.domains.find { it.value.slug == slug }?.value
    }

    private void _configureUrlMappings() {
        log.debug "Trying to change de URL's to support the configured property"

        def holder
        try {
            holder = grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER")
        } catch(NoSuchBeanDefinitionException e){
            log.debug "${e.message}"
        }

        if (!holder) {
            holder = grailsApplication.mainContext.getBean("grailsUrlMappingsHolder")
        }

        // Find Admin Url mappings
        def mappings = grailsApplication.urlMappingsClasses
        def artifact = mappings.find { it.fullName == "GrailsAdminUrlMappings" }

        if (artifact) {
            def GrailsAdminUrlMappings = artifact.clazz

            // Changes the root uri
            def mappingClosure = GrailsAdminUrlMappings.getDynamicUrlMapping(this.accessRoot)

            // Re-evaluate url mappings closure
            UrlMappingEvaluator evaluator = new DefaultUrlMappingEvaluator(grailsApplication.mainContext);
            List<UrlMapping> newMappings = evaluator.evaluateMappings(mappingClosure);

            // Remove old mappings and replace with the new re-evaluated
            holder.cachedMatches.clear()
            holder.mappings.eachWithIndex { it, idx ->
                if (it instanceof RegexUrlMapping) {
                    if (it.urlData.urlPattern.startsWith("/${GrailsAdminUrlMappings.INTERNAL_URI}/")) {
                        def elem = newMappings.remove(0)
                        holder.mappings[idx] = elem
                    }
                }
            }
        }
    }

    private void _configureAdminRole() {
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

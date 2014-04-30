package net.kaleidos.plugins.admin.config

import groovy.util.logging.Log4j
import java.util.regex.Pattern
import org.codehaus.groovy.grails.web.mapping.RegexUrlMapping
import org.springframework.beans.factory.NoSuchBeanDefinitionException

@Log4j
class AdminConfigHolder {
    def grailsApplication

    Map<String, DomainConfig> domains = [:]
    String accessRoot = "admin"
    String role = "ROLE_ADMIN"

    Closure domainDsl = null

    public AdminConfigHolder(ConfigObject config=null) {
        if (config != null) {
            if (config.grails.plugin.admin.domains) {
                this.domainDsl = config.grails.plugin.admin.domains
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

    public DomainConfig getDomainConfig(Object object) {
        return this.domains[object.class.name]
    }

    public DomainConfig getDomainConfig(Class objClass) {
        return this.domains[objClass.name]
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

        holder.cachedMatches.clear()
        holder.mappings.each {
            if (it instanceof RegexUrlMapping) {
                if (it.urlData.urlPattern.startsWith("/grails-url-admin/")) {
                    it.patterns.eachWithIndex { value, index ->
                        log.debug "$index -> $value"
                        it.patterns[index] =
                            Pattern.compile(value.toString().replace("grails-url-admin", this.accessRoot))
                    }
                    log.debug "${it.patterns}"
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
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginadmin", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginadmin.*", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminpluginadmin/**", [this.role], null)
        } catch (NoSuchBeanDefinitionException e) {
            log.error "No configured Spring Security"
        } catch (ClassNotFoundException e) {
            log.error "No configured Spring Security"
        }
    }

    private _configureDomainClasses(){
        if (!this.domainDsl) {
            return;
        }
        println "Configuring domain classes"
        def dsl = new DomainConfigurationDsl(this.domainDsl)
        dsl.grailsApplication = this.grailsApplication
        dsl.execute()
        this.domains = dsl.domains
        println "DOMAIN: ${this.domains}"
    }
}

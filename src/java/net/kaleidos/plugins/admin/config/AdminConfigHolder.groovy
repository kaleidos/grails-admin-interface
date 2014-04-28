package net.kaleidos.plugins.admin.config

import groovy.util.logging.Log4j
import java.util.regex.Pattern
import org.codehaus.groovy.grails.web.mapping.RegexUrlMapping
import org.springframework.beans.factory.NoSuchBeanDefinitionException

@Log4j
class AdminConfigHolder {
    def grailsApplication
    def objectDefinitionSource

    List<String> domains = []
    String accessRoot = "admin"
    String role = "ROLE_ADMIN"

    AdminConfigHolder(ConfigObject config=null) {
        if (config != null) {
            this.domains = config.grails.plugin.admin.domains.collect { it }

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

        log.debug "Loading config. Domains: ${this.domains}"
        log.debug "Loading config. Access Root: ${this.accessRoot}"
        log.debug "Loading config. Role: ${this.role}"
    }

    void validateConfiguration() {
        _validateDomains(this.domains)
    }


    void _validateDomains(List domains) {
        domains.each { configuredDomain ->
            if (!grailsApplication.domainClasses.find { it.fullName == configuredDomain } ) {
                throw new RuntimeException("The class ${configuredDomain} doesn't match with any domain class")
            }
        }
    }


    //void afterPropertiesSet() throws Exception {
    void initialize() {
        _configureUrlMappings()
        _configureAdminRole()
    }

    void _configureUrlMappings() {
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

    void _configureAdminRole() {
        try {
            // We use reflection so it doesn't have a compile-time dependency
            def clazz = Class.forName("grails.plugin.springsecurity.InterceptedUrl")
            def httpMethodClass = Class.forName("org.springframework.http.HttpMethod")
            def constructor = clazz.getConstructor(String.class, Collection.class, httpMethodClass)
            def newUrl = constructor.&newInstance
            objectDefinitionSource.compiled << newUrl("/grailsadminplugin", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugin.*", [this.role], null)
            objectDefinitionSource.compiled << newUrl("/grailsadminplugin/**", [this.role], null)
        } catch (ClassNotFoundException e) {
            log.error "No configured Spring Security"
        }
    }
}

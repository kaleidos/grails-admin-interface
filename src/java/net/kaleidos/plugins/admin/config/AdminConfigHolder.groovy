package net.kaleidos.plugins.admin.config

import groovy.util.logging.Log4j
import java.util.regex.Pattern
import org.springframework.beans.factory.InitializingBean


@Log4j
class AdminConfigHolder implements InitializingBean{
    def grailsApplication

    List<String> domains = []
    String accessRoot = "admin"
    String role = "ROLE_ADMIN"

    AdminConfigHolder(ConfigObject config) {
        this.domains = config.grails.plugin.admin.domains.collect { it }
        this.accessRoot = "${config.grails.plugin.admin.access_root}"

        // Remove the starting slash for the access root
        if (this.accessRoot.startsWith("/")) {
            this.accessRoot = this.accessRoot.substring(1)
        }
        this.role = "${config.grails.plugin.admin.role}"

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


    void afterPropertiesSet() throws Exception {
        log.debug "Trying to change de URL's to support the configured property"

        def holder = grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER")

        holder.cachedMatches.clear()
        def index = holder.mappings.findIndexOf { it.match("/grails-url-admin") }

        log.debug "Initializing value: ${holder.mappings[index].patterns}"

        holder.mappings[index].patterns.eachWithIndex { value, i ->
            holder.mappings[index].patterns[i] = Pattern.compile(value.toString().replace("grails-url-admin", this.accessRoot))
        }

        log.debug "Result of patterns: ${holder.mappings[index].patterns}"
    }
}

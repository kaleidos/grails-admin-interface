package net.kaleidos.plugins.admin

import java.util.regex.Pattern

@org.springframework.security.access.annotation.Secured(['IS_AUTHENTICATED_REMEMBERED'])
class DashboardController {
    def index() {

        /*
        println "Trying to change de URL's to support the configured property"

        def holder = grailsApplication.mainContext.getBean("org.grails.internal.URL_MAPPINGS_HOLDER")

        holder.cachedMatches.clear()

        println holder.mappings

        def index = holder.mappings.findIndexOf { it.match("/grails-url-admin") }

        println "Initializing value: ${holder.mappings[index].patterns}"

        holder.mappings[index].patterns.eachWithIndex { value, i ->
            holder.mappings[index].patterns[i] = Pattern.compile(value.toString().replace("grails-url-admin", "ddd"))
        }

        println "Result of patterns: ${holder.mappings[index].patterns}"
        */

        render "DASHBOARD"
    }
}

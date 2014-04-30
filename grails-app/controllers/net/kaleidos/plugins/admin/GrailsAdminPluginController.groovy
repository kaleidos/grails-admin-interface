package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.web.sitemesh.GroovyPageLayoutFinder

import org.springframework.security.access.annotation.Secured

@Secured(["ROLE_ADMIN"])
class GrailsAdminPluginController {
    def objectDefinitionSource

    def index() {
        render view:'/grailsAdmin/dashboard',  model:[]
    }

    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }

    def menu() {
        render view:'/grailsAdmin/includes/menu',  model:[]
    }
}

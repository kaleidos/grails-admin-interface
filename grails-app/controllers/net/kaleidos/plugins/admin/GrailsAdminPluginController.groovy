package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.web.sitemesh.GroovyPageLayoutFinder

class GrailsAdminPluginController {
    def objectDefinitionSource
    def adminConfigHolder    

    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }

    def menu() {
        render view:'/grailsAdmin/includes/menu',  model:[domainClasses: adminConfigHolder.domains.values()]
    }
    
    def dashboard() {
        render view:'/grailsAdmin/dashboard',  model:[domainClasses: adminConfigHolder.domains.values()]
    }
    
    def list() {
        render view:'/grailsAdmin/list',  model:[]
    }
    
    def edit() {
        render view:'/grailsAdmin/edit',  model:[]
    }
    
    def add() {
        render view:'/grailsAdmin/add',  model:[]
    }

}

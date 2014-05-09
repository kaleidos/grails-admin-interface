import grails.util.Holders
import net.kaleidos.plugins.admin.config.AdminConfigHolder

class AdminGrailsPlugin {
    def version = "0.1"

    def grailsVersion = "2.0.0 > *"

    def loadAfter = ["springSecurityCore"]

    def watchedResources = [
        "file:./grails-app/controllers/**/*Controller.groovy",
        "file:./grails-app/domain/**/*.groovy",
        "file:./grails-app/admin/**/*.groovy",
        "file:./grails-app/conf/Config.groovy",
    ]

    def pluginExcludes = [
        "grails-app/domain/**"
    ]

    def title = "Grails Admin"
    def author = "Kaleidos Open Source"
    def authorEmail = "hello@kaleidos.net"
    def description = "Administration backoffice for Grails applications"

    def documentation = "http://grails.org/plugin/admin"

    def license = "APACHE"

    def organization = [ name: "Kaleidos Open Source", url: "http://www.kaleidos.net" ]

    def developers = [
        [ name: "Pablo Alba", email: "pablo.alba@kaleidos.net" ],
        [ name: "Antonio de la Torre", email: "antonio.delatorre@kaleidos.net" ],
        [ name: "Daniel Herrero", email: "daniel.herrero@kaleidos.net" ],
        [ name: "Juan Francisco Alcantara", email: "juanfran.alcantara@kaleidos.net" ],
        [ name: "Alonso Torres", email: "alonso.torres@kaleidos.net" ],
        [ name: "Sandra Alarcon", email: "alejandra.alarconh@gmail.com" ]
    ]

    def issueManagement = [ system: "Github", url: "https://github.com/grails-admin/grails-admin" ]

    def scm = [ url: "https://github.com/grails-admin/grails-admin.git" ]

    def doWithSpring = {
        adminConfigHolder(AdminConfigHolder, Holders.config) {
            grailsApplication = ref("grailsApplication")
        }
    }

    def doWithApplicationContext = { ctx ->
        // We should initialize the config here to load after spring security
        ctx.adminConfigHolder.initialize()
    }

    def onChange = { event->
        event.ctx.adminConfigHolder.initialize()
    }
}

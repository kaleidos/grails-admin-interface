import net.kaleidos.plugins.admin.config.AdminConfigHolder

@groovy.util.logging.Log4j
class AdminInterfaceGrailsPlugin {
    def version = "0.6.1"

    def grailsVersion = "2.0.0 > *"

    def loadAfter = ["springSecurityCore"]

    def watchedResources = [
        "file:./grails-app/controllers/**/*Controller.groovy",
        "file:./grails-app/domain/**/*.groovy",
        "file:./grails-app/admin/**/*.groovy",
        "file:./grails-app/conf/Config.groovy",
    ]

    def pluginExcludes = [
        "grails-app/admin/**",
        "grails-app/domain/**"
    ]

    def title = "Grails Admin Interface"
    def author = "Kaleidos Open Source"
    def authorEmail = "grails-admin@kaleidos.net"

    def description = """
    Grails Admin allows you to easily create an administration back-office with default styles and
    operations. You cand extend and configure this administrator to improve the experience of your
    users.

    The default admin also provides an API to ease the integration with already existing
    administration back-offices and integration with Spring Security (version 1 and 2) to use the
    security behaviour of your application.

    This is a similar project to those existing in others MVC frameworks such as Django Admin,
    Sonata Admin or Rails Active Admin.
    """

    def documentation = "http://kaleidos.github.io/grails-admin-interface"

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

    def issueManagement = [ system: "Github", url: "https://github.com/kaleidos/grails-admin-interface" ]

    def scm = [ url: "https://github.com/kaleidos/grails-admin-interface.git" ]

    def doWithSpring = {
        adminConfigHolder(AdminConfigHolder)
    }

    def doWithApplicationContext = { ctx ->
        ctx.adminConfigHolder.initialize()
    }

    def onChange = { event->
        event.ctx.adminConfigHolder.initialize()
    }

    def onConfigChange = { event->
        event.ctx.adminConfigHolder.initialize()
    }
}

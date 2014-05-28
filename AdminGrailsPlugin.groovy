import grails.util.Holders
import net.kaleidos.plugins.admin.config.AdminConfigHolder
import org.springframework.beans.factory.NoSuchBeanDefinitionException

@groovy.util.logging.Log4j
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
        adminConfigHolder(AdminConfigHolder)
    }

    def doWithApplicationContext = { ctx ->
        // We should initialize the config here to load after spring security
        ctx.adminConfigHolder.initialize()
        _configureAdminRole()
    }

    def onChange = { event->
        event.ctx.adminConfigHolder.initialize()
        _configureAdminRole()
    }


    private _configureAdminRole() {
        try {
            // try to get the Secured annotation of Spring security 2
            Class.forName("grails.plugin.springsecurity.annotation.Secured")
            _configureAdminRoleSecurity2()
        } catch (Throwable e) {
            // If it fails we configure for spring security core 1.x
            _configureAdminRoleSecurity1()
        }
    }

    private void _configureAdminRoleSecurity1() {
        try {
            def role = Holders.config.grails.plugin.admin.role?:"ROLE_ADMIN"

            def clazz =  Class.forName("org.springframework.security.access.SecurityConfig")
            def constructor = clazz.getConstructor(String.class)
            def newConfig = constructor.&newInstance

            def objectDefinitionSource = Holders.grailsApplication.mainContext.getBean("objectDefinitionSource")
            objectDefinitionSource.storeMapping("/grailsadminpluginui/**", [newConfig(role)] as Set)
            objectDefinitionSource.storeMapping("/grailsadminpluginapi/**", [newConfig(role)] as Set)
            objectDefinitionSource.storeMapping("/grailsadminplugincallbackapi/**", [newConfig(role)] as Set)
        } catch (NoSuchBeanDefinitionException e) {
            log.error "No configured Spring Security"
        } catch (ClassNotFoundException e) {
            log.error "No configured Spring Security"
        }
    }

    private void _configureAdminRoleSecurity2() {
        try {
            def role = Holders.config.grails.plugin.admin.role?:"ROLE_ADMIN"

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
        } catch (NoSuchBeanDefinitionException e) {
            log.error "No configured Spring Security"
        } catch (ClassNotFoundException e) {
            log.error "No configured Spring Security"
        }
    }
}

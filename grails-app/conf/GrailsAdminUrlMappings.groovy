
import grails.util.Holders

class GrailsAdminUrlMappings {
    static mappings = {
        group "/grails-url-admin", {
            "/" { controller = "grailsAdminPlugin" ; action="index" }
            "/$adminController?/$adminAction?/$id?" { controller = "grailsAdminPlugin" ; action="adminMethod" }
        }
    }
}

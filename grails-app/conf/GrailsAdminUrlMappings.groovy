
import grails.util.Holders

class GrailsAdminUrlMappings {
    static mappings = {
        group "/grails-url-admin", {
            "/" { controller = "dashboard" ; action="index" }
            "/$adminController?/$adminAction?/$id?" { controller = "admin" ; action="adminMethod" }
        }
    }
}

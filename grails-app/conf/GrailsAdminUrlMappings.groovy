
import grails.util.Holders

class GrailsAdminUrlMappings {
    static mappings = {
        group "/grails-url-admin", {
            "/" { controller = "dashboard" ; action="index" }
        }
    }
}

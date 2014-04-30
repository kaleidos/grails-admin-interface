
import grails.util.Holders

class GrailsAdminUrlMappings {
    static mappings = {
        group "/grails-url-admin", {
            "/" { controller = "grailsAdminPlugin" ; action="index" }
            "/api" { controller = "grailsAdminPluginApi" ; action = "listDomains" }
            "/api/$domain?/$id?" {
                controller = "grailsAdminPluginApi"
                action=[ GET:"getAdminAction",
                         POST:"postAdminAction",
                         DELETE:"deleteAdminAction",
                         PUT:"putAdminAction"]
            }
            "/web/$adminController?/$adminAction?/$id?" { controller = "grailsAdminPlugin" ; action="adminMethod" }
        }
    }
}

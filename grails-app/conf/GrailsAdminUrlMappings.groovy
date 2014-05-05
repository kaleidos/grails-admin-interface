
import grails.util.Holders

class GrailsAdminUrlMappings {
    static mappings = {
        group "/grails-url-admin", {
            name dashboard: "/" { controller = "grailsAdminPlugin" ; action="index" }
            "/$adminController?/$adminAction?/$id?" { controller = "grailsAdminPlugin" ; action="adminMethod" }
            
            name list: "/list/$slug" { controller = "grailsAdminPlugin" ; action="list" }
            name edit: "/edit" { controller = "grailsAdminPlugin" ; action="edit" }
            name add: "/add" { controller = "grailsAdminPlugin" ; action="add" }
                    
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


import grails.util.Holders

class GrailsAdminUrlMappings {
    static mappings = {
        group "/grails-url-admin", {
            name dashboard: "/" { controller = "grailsAdminPlugin" ; action="dashboard" }
            "/$adminController?/$adminAction?/$id?" { controller = "grailsAdminPlugin" ; action="adminMethod" }

            name list: "/list/$slug/$page?" { controller = "grailsAdminPlugin" ; action="list" }
            name edit: "/edit" { controller = "grailsAdminPlugin" ; action="edit" }
            name add: "/add/$slug" { controller = "grailsAdminPlugin" ; action=[GET:"add", POST:"addAction"] }

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

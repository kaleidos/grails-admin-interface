class GrailsAdminUrlMappings {
    private static final INTERNAL_URI = "grails-url-admin"

    static mappings = getDynamicUrlMapping(INTERNAL_URI)

    static getDynamicUrlMapping(String baseUrl) {
        return {
            group "/${baseUrl}", {
            name dashboard: "/" { controller = "grailsAdminPlugin" ; action="dashboard" }


            name delete: "/delete/$slug" { controller = "grailsAdminPlugin" ; action="delete" }
            name list: "/list/$slug/$page?" { controller = "grailsAdminPlugin" ; action="list" }
            name edit: "/edit/$slug/$id" { controller = "grailsAdminPlugin" ; action=[GET:"edit", POST:"editAction"] }
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
}

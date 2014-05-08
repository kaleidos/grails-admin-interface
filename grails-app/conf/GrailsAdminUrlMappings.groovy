class GrailsAdminUrlMappings {
    private static final INTERNAL_URI = "grails-url-admin"

    static mappings = getDynamicUrlMapping(INTERNAL_URI)

    static getDynamicUrlMapping(String baseUrl) {
        return {
            group "/${baseUrl}", {
                name grailsAdminDashboard: "/" { controller = "grailsAdminPlugin" ; action="dashboard" }

                name grailsAdminDelete: "/delete/$slug" { controller = "grailsAdminPlugin" ; action="delete" }
                name grailsAdminList: "/list/$slug/$page?" { controller = "grailsAdminPlugin" ; action="list" }
                name grailsAdminEdit: "/edit/$slug/$id" { controller = "grailsAdminPlugin" ; action=[GET:"edit", POST:"editAction"] }
                name grailsAdminAdd: "/add/$slug" { controller = "grailsAdminPlugin" ; action=[GET:"add", POST:"addAction"] }

                // API
                name grailsAdminApiDashboard: "/api" { controller = "grailsAdminPluginApi" ; action = "listDomains" }
                name grailsAdminApiAction: "/api/$slug?/$id?" { controller = "grailsAdminPluginApi" ; action=[ GET:"getAdminAction", POST:"postAdminAction", DELETE:"deleteAdminAction", PUT:"putAdminAction"] }
            }
        }
    }
}

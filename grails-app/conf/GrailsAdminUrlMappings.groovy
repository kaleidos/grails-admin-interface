class GrailsAdminUrlMappings {
    private static final INTERNAL_URI = "grails-url-admin"

    static mappings = getDynamicUrlMapping(INTERNAL_URI)

    static getDynamicUrlMapping(String baseUrl) {
        return {
            group "/${baseUrl}", {
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
}

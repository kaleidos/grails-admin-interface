package net.kaleidos.plugins.admin

class GrailsAdminUrlMappings {
    private static final String ADMIN_NAMESPACE = "admin"
    static mappings = getDynamicUrlMapping()

    static getDynamicUrlMapping() {
        def baseUrl = grails.util.Holders.config.grails.plugin.admin.accessRoot
        if (!baseUrl) {
            baseUrl = "admin"
        } else if (baseUrl.startsWith("/")) {
            baseUrl = baseUrl.substring(1)
        }

        return {
            // UI
            name grailsAdminDashboard: "/${baseUrl}/" { controller = "grailsAdminPluginUI" ; action="dashboard"; namespace = ADMIN_NAMESPACE }
            name grailsAdminDelete: "/${baseUrl}/delete/$slug" { controller = "grailsAdminPluginUI" ; action="delete" ; namespace = ADMIN_NAMESPACE}
            name grailsAdminList: "/${baseUrl}/list/$slug/$page?" { controller = "grailsAdminPluginUI" ; action="list" ; namespace = ADMIN_NAMESPACE }
            name grailsAdminEdit: "/${baseUrl}/edit/$slug/$id" { controller = "grailsAdminPluginUI" ; action=[GET:"edit", POST:"editAction"] ; namespace = ADMIN_NAMESPACE }
            name grailsAdminAdd: "/${baseUrl}/add/$slug" { controller = "grailsAdminPluginUI" ; action=[GET:"add", POST:"addAction"] ; namespace = ADMIN_NAMESPACE }

            // Callbacks
            name grailsAdminSuccessEdit: "/${baseUrl}/success-edit/$slug" { controller = "grailsAdminPluginCallbackApi" ; action="successSave" ; namespace = ADMIN_NAMESPACE}
            name grailsAdminSuccessList: "/${baseUrl}/success-list/$slug" { controller = "grailsAdminPluginCallbackApi" ; action="successList" ; namespace = ADMIN_NAMESPACE}
            name grailsAdminSuccessNew: "/${baseUrl}/success-new/$slug" { controller = "grailsAdminPluginCallbackApi" ; action="successNew" ; namespace = ADMIN_NAMESPACE}
            name grailsAdminSuccessDelete: "/${baseUrl}/success-delete/$slug" { controller = "grailsAdminPluginCallbackApi" ; action="successDelete" ; namespace = ADMIN_NAMESPACE}

            // API
            name grailsAdminApiDashboard: "/${baseUrl}/api" { controller = "grailsAdminPluginApi" ; action = "listDomains" ; namespace = ADMIN_NAMESPACE }

            name grailsAdminCountApiAction: "/${baseUrl}/api/$slug/count" { controller = "grailsAdminPluginApi" ; action="countAdminAction" ; namespace = ADMIN_NAMESPACE }

            name grailsAdminApiAction: "/${baseUrl}/api/$slug?/$id?" {
                controller = "grailsAdminPluginApi"
                action=[ GET:"getAdminAction", POST:"postAdminAction", DELETE:"deleteAdminAction", PUT:"putAdminAction"]
                namespace = ADMIN_NAMESPACE
            }
            name grailsAdminRelatedApiAction: "/${baseUrl}/api/$slug/$id/$propertyName/$id2" {
                controller = "grailsAdminPluginApi" ;
                action=[ DELETE:"deleteRelatedAdminAction", PUT:"putRelatedAdminAction" ]
                namespace = ADMIN_NAMESPACE
            }
        }
    }
}

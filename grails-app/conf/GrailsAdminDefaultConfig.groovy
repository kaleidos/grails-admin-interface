// Default configuration: grails.plugin.admin
defaultAdminConfig {
    domains = []
    accessRoot = "/admin"

    security {
        role = "ROLE_ADMIN"
        forbidUnsecureProduction = true
    }

    allowDefaultConfig = false
}

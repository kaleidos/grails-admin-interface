// Default configuration: grails.plugin.admin
admin {
    domains = []
    accessRoot = "/admin"

    security {
        role = "ROLE_ADMIN"
        forbidUnsecureProduction = true
    }

    allowDefaultConfig = false
}

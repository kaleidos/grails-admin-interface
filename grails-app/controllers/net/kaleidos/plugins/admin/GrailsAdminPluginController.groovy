package net.kaleidos.plugins.admin

class GrailsAdminPluginController {
    def objectDefinitionSource

    def index() {
        render "DASHBOARD"
    }

    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }
}

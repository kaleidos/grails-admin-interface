package net.kaleidos.plugins.admin

class GrailsAdminPluginCallbackApiController {
    static namespace = "admin"

    def successSave(String slug) {
        flash.success = g.message(code:"grailsAdminPlugin.add.success")
        redirect(mapping: 'grailsAdminEdit', params: [slug: slug, id: params.id] )
    }

    def successList(String slug) {
        flash.success = g.message(code:"grailsAdminPlugin.add.success")
        redirect(mapping: 'grailsAdminList', params: [slug: slug] )
    }

    def successNew(String slug) {
        flash.success = g.message(code:"grailsAdminPlugin.add.success")
        redirect(mapping: 'grailsAdminAdd', params: [slug: slug] )
    }

    def successDelete(String slug) {
        flash.success = g.message(code:"grailsAdminPlugin.action.delete.success")
        redirect(mapping: 'grailsAdminList', params: [slug: slug] )
    }
}

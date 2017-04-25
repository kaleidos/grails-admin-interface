package net.kaleidos.plugins.admin.widget.relation

import groovy.xml.MarkupBuilder
import net.kaleidos.plugins.admin.widget.Widget

abstract class RelationPopupWidget extends Widget{
    def grailsLinkGenerator
    def adminConfigHolder
    def groovyPageRenderer

    public RelationPopupWidget() {
        def ctx = grails.util.Holders.applicationContext
        grailsLinkGenerator = ctx.grailsLinkGenerator
        adminConfigHolder = ctx.adminConfigHolder
        groovyPageRenderer = ctx.groovyPageRenderer
    }


    public getUuid() {
        return "${internalAttrs.domainClass.name}_${internalAttrs.propertyName}".replaceAll("\\.", "_").toLowerCase()
    }

    String renderAfterForm(relationConfig) {
        Writer writer = new StringWriter()
        MarkupBuilder builder = new MarkupBuilder(writer)

        builder.div id:"new-$uuid", tabindex:"-1", view: "relationPopupWidgetNew", role:"dialog", "aria-labelledby":"confirmLabel", "aria-hidden":"true",  "grailsadmin-remote": "enabled", class:"modal fade", "data-field":"${internalAttrs.propertyName}", {
            div class:"modal-dialog", {
                div class:"modal-content", {
                    div class:"modal-header", {
                        buton type:"button", "data-dismiss":"modal", "aria-hidden":"true", class:"close", {
                            mkp.yield "x"
                        }
                        h4 id:"confirmLabel", class:"modal-title", {
                            mkp.yield "Add ${internalAttrs["propertyName"]}"
                        }
                    }
                    div class:"modal-body", {
                        mkp.yieldUnescaped groovyPageRenderer.render(template: '/grailsAdmin/addForm', model: [domain: relationConfig, embedded:true])
                    }
                    div class:"modal-footer", {
                        button type:"button", "data-dismiss":"modal", class:"btn btn-default", { mkp.yield "Close" }
                        button type:"button", class:"btn btn-plus btn-success js-relation-popup-widget-new-save-action", { mkp.yield "Save" }
                    }
                }
            }
        }
        return writer.toString()
    }

}

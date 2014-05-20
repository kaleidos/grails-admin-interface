package net.kaleidos.plugins.admin.widget.relation

import net.kaleidos.plugins.admin.widget.Widget
import groovy.xml.MarkupBuilder

abstract class AbstractRelationPopup extends Widget {
    def grailsLinkGenerator
    def adminConfigHolder
    def groovyPageRenderer

    public AbstractRelationPopup() {
        def ctx = grails.util.Holders.applicationContext
        grailsLinkGenerator = ctx.grailsLinkGenerator
        adminConfigHolder = ctx.adminConfigHolder
        groovyPageRenderer = ctx.groovyPageRenderer
    }

    @Override
    String render() {
        if (htmlAttrs.disallowRelationships) {
            return "<p>Disabled relationship due to be inside an embedded dialog</p>"
        }

        def uuid =  UUID.randomUUID().toString()
        def relationConfig = adminConfigHolder.getDomainConfigForProperty(internalAttrs.domainClass, internalAttrs.propertyName)

        return doRenderWithParent(uuid) { builder ->
            builder.div id:"add-$uuid", tabindex:"-1", role:"dialog", "aria-labelledby":"confirmLabel", "aria-hidden":"true", class:"modal fade", {
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
                            mkp.yield "Loading..."
                        }
                        div class:"modal-footer", {
                            button type:"button", "data-dismiss":"modal", class:"btn btn-default", { mkp.yield "Close" }
                            button type:"button", "data-dismiss":"modal", class:"btn btn-plus js-relationtablewidget-add-action", { mkp.yield "Save" }
                        }
                    }
                }
            }
            if (relationConfig) {
                builder.div id:"new-$uuid", tabindex:"-1", role:"dialog", "aria-labelledby":"confirmLabel", "aria-hidden":"true", class:"modal fade", {
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
                                button type:"button", "data-dismiss":"modal", class:"btn btn-plus js-relationtablewidget-add-action", { mkp.yield "Save" }
                            }
                        }
                    }
                }
            }
        }
    }

    abstract String doRenderWithParent(String uuid, Closure parent)
}

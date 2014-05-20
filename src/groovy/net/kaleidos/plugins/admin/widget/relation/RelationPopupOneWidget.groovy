package net.kaleidos.plugins.admin.widget.relation

import net.kaleidos.plugins.admin.widget.Widget
import groovy.xml.MarkupBuilder

class RelationPopupOneWidget extends Widget{
    def grailsLinkGenerator
    def adminConfigHolder
    def groovyPageRenderer

    public RelationPopupOneWidget() {
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

        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def relationObject = internalAttrs.domainObject?."${internalAttrs.propertyName}"
        def relationConfig = adminConfigHolder.getDomainConfigForProperty(internalAttrs.domainClass, internalAttrs.propertyName)
        def slug = relationConfig?.slug
        def action = grailsLinkGenerator.link(mapping:"grailsAdminApiAction", method: "put", params:[slug:slug])

        // Links

        builder.div class:"relation-popupone-widget ", view:"relationpopuponewidget", "data-method":"put", action:action, {
            input type:'hidden', class:'js-one-rel-value', name:"${internalAttrs.propertyName}", value: value

            _detailLink(slug, relationObject, builder)
            _buttons(slug, relationObject, delegate)
        }
        return writer.toString()
    }

    def _detailLink(slug, relationObject, builder) {
        def editLink = ''
        if (slug) {
            editLink = grailsLinkGenerator.link(mapping:"grailsAdminEdit", params:[slug:slug, id: (relationObject?.id)?:0])
        }

        builder.a href:editLink, class:'js-one-rel-text', name:"${internalAttrs.propertyName}", {
            if (value) {
                mkp.yield "${relationObject}".encodeAsHTML()
            } else {
                mkp.yield "<< empty >>"
            }
        }
    }

    def _buttons(slug, relationObject, builder) {
        def listApi = ''
        if (slug) {
            listApi = grailsLinkGenerator.link(mapping:"grailsAdminApiAction", method:"get", params:[slug:slug])
        }

        String display = (relationObject)?"block":"none"

        builder.div class:"btn-group", {
            a href:"#", class:"btn btn-default js-relationpopuponewidget-list", "data-toggle":"modal", "data-url":listApi, {
                span class:"glyphicon glyphicon-list", { mkp.yield " "}
                mkp.yield " List"

            }
            a href:"#", class:"btn btn-default js-relationpopuponewidget-new", "data-toggle":"modal", "data-target":"#new-$uuid", {
                span class:"glyphicon glyphicon-plus", { mkp.yield " "}
                mkp.yield " New"

            }
            a href:"#", class:"btn btn-default js-relationpopuponewidget-delete", style:"display:${display};",{
                span class:"glyphicon glyphicon-trash", { mkp.yield " "}
                mkp.yield " Delete"

            }
        }
    }

    @Override
    String renderAfterForm() {
        def relationConfig = adminConfigHolder.getDomainConfigForProperty(internalAttrs.domainClass, internalAttrs.propertyName)

        if (relationConfig && !htmlAttrs.disallowRelationships) {
            def writer = new StringWriter()
            def builder = new MarkupBuilder(writer)

            builder.div id:"new-$uuid", tabindex:"-1", role:"dialog", "aria-labelledby":"confirmLabel", "aria-hidden":"true", class:"modal fade", "data-field":"${internalAttrs.propertyName}", {
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
                            button type:"button", class:"btn btn-plus js-relationtablewidget-save-action", { mkp.yield "Save" }
                        }
                    }
                }
            }
            return writer.toString()
        }
    }

    @Override
    List<String> getAssets() {
        [ 'js/admin/relationpopup.js',
          'js/admin/relationpopuponewidget.js'
        ]
    }

    public getUuid() {
        return "${internalAttrs.domainClass.name}_${internalAttrs.propertyName}".replaceAll("\\.", "_").toLowerCase()
    }
}

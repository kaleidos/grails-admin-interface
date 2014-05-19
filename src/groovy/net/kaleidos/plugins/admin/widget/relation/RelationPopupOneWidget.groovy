package net.kaleidos.plugins.admin.widget.relation

import net.kaleidos.plugins.admin.widget.Widget
import groovy.xml.MarkupBuilder

class RelationPopupOneWidget extends AbstractRelationPopup {
    def grailsLinkGenerator
    def adminConfigHolder

    public RelationPopupOneWidget() {
        def ctx = grails.util.Holders.applicationContext
        grailsLinkGenerator = ctx.grailsLinkGenerator
        adminConfigHolder = ctx.adminConfigHolder
    }

    @Override
    String doRenderWithParent(Closure parent) {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def relationObject = internalAttrs.domainObject?."${internalAttrs.propertyName}"
        def relationConfig = adminConfigHolder.getDomainConfigForProperty(internalAttrs.domainClass, internalAttrs.propertyName)
        def slug = relationConfig?.slug
        def action = grailsLinkGenerator.link(mapping:"grailsAdminApiAction", method: "put", params:[slug:slug])

        // Links

        builder.div class:"relation-popupone-widget ", view:"relationpopuponewidget", "data-method":"put", action:action, {
            input type:'hidden', class:'js-one-rel-value', name:"${internalAttrs.propertyName}"

            _detailLink(slug, relationObject, builder)
            _buttons(slug, relationObject, delegate)

            parent(delegate)
        }
        return writer.toString()
    }

    def _detailLink(slug, relationObject, builder) {
        def editLink = ''
        if (slug) {
            editLink = grailsLinkGenerator.link(mapping:"grailsAdminEdit", params:[slug:slug, id: (relationObject?.id)?:0])
        }

        builder.a href:editLink, class:'js-one-rel-text', {
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
                span class:"glyphicon glyphicon-list", {
                    mkp.yield "List"
                }
            }
            a href:"#", class:"btn btn-default js-relationpopuponewidget-new", {
                span class:"glyphicon glyphicon-plus", {
                    mkp.yield "New"
                }
            }
            a href:"#", class:"btn btn-default js-relationpopuponewidget-delete", style:"display:${display};",{
                span class:"glyphicon glyphicon-trash", {
                    mkp.yield "Delete"
                }
            }
        }
    }
}

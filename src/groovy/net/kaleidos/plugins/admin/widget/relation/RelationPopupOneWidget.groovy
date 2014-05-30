package net.kaleidos.plugins.admin.widget.relation

import net.kaleidos.plugins.admin.widget.Widget
import groovy.xml.MarkupBuilder

class RelationPopupOneWidget extends RelationPopupWidget{

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

        builder.div class:"relation-popupone-widget ", view:"relationPopupOneWidgetField", "data-method":"put", action:action, {
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
        def countApi = ''

        if (slug) {
            listApi = grailsLinkGenerator.link(mapping:"grailsAdminApiAction", method:"get", params:[slug:slug])
            countApi = grailsLinkGenerator.link(mapping:"grailsAdminCountApiAction", method:"get", params:[slug:slug])
        }

        String display = (relationObject)?"block":"none"

        builder.div class:"btn-group", {
            a href:"#", class:"btn btn-default js-relationpopuponewidget-list", "data-toggle":"modal", "data-url":listApi, "data-url-count": countApi, {
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
            return super.renderAfterForm(relationConfig)
        }
    }

    @Override
    List<String> getAssets() {
        [ 'js/admin/relationpopup.js',
          'js/admin/relationPopupOneWidgetField.js',
          'js/admin/relationPopupWidgetNew.js',
          'js/admin/relationPopupWidgetList.js',
          'grails-admin/templates/grails-admin-modal.handlebars',
          'grails-admin/templates/grails-admin-list.handlebars',
          'grails-admin/templates/grails-admin-pagination.handlebars'
        ]
    }


    public void updateValue() {
        if (value) {
            def object =  internalAttrs['relatedDomainClass'].get(value as Long)
            updateValue(object)
        } else {
            updateValue(null)
        }
    }
}

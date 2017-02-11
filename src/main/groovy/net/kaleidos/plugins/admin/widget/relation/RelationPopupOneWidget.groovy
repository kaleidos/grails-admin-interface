package net.kaleidos.plugins.admin.widget.relation

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

        builder.div view:"relationPopupOneWidgetField", {
            div class:"relation-popupone-widget ", "data-method":"put", action:action, {
                _detailLink(slug, relationObject, builder)
                _buttons(slug, relationObject, delegate)
            }
            div {
                def attrs = htmlAttrs.clone()
                attrs << ["type": 'text']
                attrs << ["value": value]
                attrs << ["class": 'hidden js-one-rel-value form-control']
                attrs << ["name": "${internalAttrs.propertyName}"]

                input (attrs)
            }
        }
        return writer.toString()
    }

    def _detailLink(slug, relationObject, builder) {
        def editLink = ''
        if (slug) {
            editLink = grailsLinkGenerator.link(mapping:"grailsAdminEdit", params:[slug:slug, id: (relationObject?.id)?:0])
            builder.a href:editLink, class:'js-one-rel-text', name:"${internalAttrs.propertyName}", {
                if (value) {
                    mkp.yield "${relationObject}".encodeAsHTML()
                } else {
                    mkp.yield "<< empty >>"
                }
            }
        } else {
            builder.label {
                if (value) {
                    mkp.yield "${relationObject}".encodeAsHTML()
                } else {
                    mkp.yield "<< empty >>"
                }
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
            def attrs = [href:"#", class:"btn btn-default js-relationpopuponewidget-list", "data-toggle":"modal", "data-url":listApi, "data-url-count": countApi]
            if (!slug) {
                attrs['disabled']='disabled'
            }
            a (attrs) {
                span class:"glyphicon glyphicon-list", { mkp.yield " "}
                mkp.yield " List"

            }


            attrs = [href:"#", class:"btn btn-default js-relationpopuponewidget-new", "data-toggle":"modal", "data-target":"#new-$uuid"]
            if (!slug) {
                attrs['disabled']='disabled'
            }
            a (attrs) {
                span class:"glyphicon glyphicon-plus", { mkp.yield " "}
                mkp.yield " New"

            }

            attrs = [href:"#", class:"btn btn-default js-relationpopuponewidget-delete", style:"display:${display};"]
            if (!slug) {
                attrs['disabled']='disabled'
            }
            a (attrs) {
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
        def results = [
            'grails-admin/css/widgets/relationpopuponewidget.css',
            //'grails-admin/js/widgets/relationpopup.js',
            'grails-admin/js/widgets/relationPopupOneWidgetField.js',
            'grails-admin/js/widgets/relationPopupWidgetNew.js',
            'grails-admin/js/widgets/relationPopupWidgetList.js',
            'grails-admin/templates/grails-admin-modal.handlebars',
            'grails-admin/templates/grails-admin-list.handlebars',
            'grails-admin/templates/grails-admin-pagination.handlebars'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
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

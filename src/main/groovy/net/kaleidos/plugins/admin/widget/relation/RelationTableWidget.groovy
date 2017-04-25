package net.kaleidos.plugins.admin.widget.relation

import groovy.xml.MarkupBuilder

class RelationTableWidget extends RelationPopupWidget{

    String render() {
        if (htmlAttrs.disallowRelationships) {
            return "<p>Disabled relationship due to be inside an embedded dialog</p>"
        }

        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def options = [:]

        if (internalAttrs["relatedDomainClass"]) {
            def domainClass = internalAttrs["relatedDomainClass"]
            def otherSideProperty = internalAttrs["grailsDomainClass"].getPropertyByName(internalAttrs['propertyName']).getOtherSide()
            def optional = otherSideProperty?otherSideProperty.isOptional():true

            def relationConfig = adminConfigHolder.getDomainConfig(domainClass)
            def slug
            def listUrl
            def countUrl

            if (relationConfig) {
                slug = relationConfig?.slug
                listUrl = grailsLinkGenerator.link(mapping: 'grailsAdminApiAction', params:[ 'slug': slug ])
                countUrl = grailsLinkGenerator.link(mapping:"grailsAdminCountApiAction", method:"get", params:[slug:slug])
            }

            value.each {id ->
                def element = domainClass.get(id)
                options[id] = element.toString()
            }

            builder.div class:"relationtablewidget clearfix", view:"relationTableWidget", {
                options.each { key, value ->
                    input type: "hidden", name:htmlAttrs['name'], value: key
                }
                _elementsTable(delegate, domainClass, options, optional, slug)
                div {
                    def attrs = [class:"btn btn-default js-relationtablewidget-list", "data-url": listUrl, "data-url-count": countUrl, href:"#"]
                    if (! relationConfig){
                        attrs['disabled'] = 'disabled'
                    }
                    a (attrs) {
                        span(class:"glyphicon glyphicon-list", "")
                        mkp.yield " List"
                    }

                    attrs = [class:"btn btn-default js-relationtablewidget-new", "data-url": listUrl, "data-url-count": countUrl, href:"#", "data-toggle":"modal","data-target":"#new-$uuid"]
                    if (! relationConfig){
                        attrs['disabled'] = 'disabled'
                    }
                    a (attrs) {
                        span(class:"glyphicon glyphicon-plus", "")
                        mkp.yield " New"
                    }
                }
            }
        }

        return writer.toString()
    }

    def _elementsTable(builder, domainClass, options, isOptional, slug) {

        def detailUrl
        if (slug) {
            detailUrl = grailsLinkGenerator.link(mapping: 'grailsAdminEdit', params:['slug':slug, 'id':0])
        }

        builder.table "data-detailurl":detailUrl, "data-property-name":internalAttrs["propertyName"], "data-optional":isOptional, class:"table table-bordered elements-table", {
            // We need an empty element so grails doesn't construct <table/> when there is no elements (it's not valid html)
            mkp.yield ""
            options.each { key, value ->
                def url
                if (slug) {
                    url = grailsLinkGenerator.link(mapping: 'grailsAdminEdit', params:['slug':slug, 'id':key])
                }
                tr {
                    td {
                        if (slug) {
                            a href: url, { mkp.yield value }
                        } else {
                            label { mkp.yield value }
                        }
                    }

                    if (isOptional && slug) {
                        td class: "list-actions", {
                            a class: "btn btn-default btn-sm js-relationtablewidget-delete", "data-value":key, "href":"#", {
                                span class:"glyphicon glyphicon-trash", {mkp.yield " "}
                                mkp.yield " Delete"

                            }
                        }
                    }
                }
            }
        }

    }

    @Override
    String renderAfterForm() {
        def relationConfig = adminConfigHolder.getDomainConfig(internalAttrs["relatedDomainClass"])
        if (relationConfig && !htmlAttrs.disallowRelationships) {
            return Object.renderAfterForm(relationConfig)
        }
    }

    List<String> getAssets() {
        def results = [
            'grails-admin/css/widgets/relationtablewidget.css',
            //'grails-admin/js/widgets/relationpopup.js',
            'grails-admin/js/widgets/relationPopupWidgetNew.js',
            'grails-admin/js/widgets/relationTableWidget.js',
            'grails-admin/js/widgets/relationPopupWidgetList.js',
            'grails-admin/templates/grails-admin-modal.handlebars',
            'grails-admin/templates/grails-admin-list.handlebars',
            'grails-admin/templates/grails-admin-selected-item.handlebars',
            'grails-admin/templates/grails-admin-pagination.handlebars'
        ]
        return results.collect { ["plugin":"admin-interface", "absolute":true, "file":it]  }
    }

    def getValueForJson() {
        def values = []
        def domainClass = internalAttrs["relatedDomainClass"]
        value.each {id ->
            def element = domainClass.get(id)
            values << element.toString()
        }
        return values
    }


    public void updateValue() {
        def domains = []
        def object = internalAttrs["domainObject"]

        if (value) {
            if (value instanceof List) {
                value.each {
                    domains << internalAttrs['relatedDomainClass'].get(it as Long)
                }
            } else {
                domains << internalAttrs['relatedDomainClass'].get(value as Long)
            }
        }

        def cap = internalAttrs.propertyName.capitalize()
        if (object."${internalAttrs.propertyName}") {
            def current = []
            def toDelete = (object."${internalAttrs.propertyName}").findAll{! (it in domains)}
            current.addAll(toDelete)
            current.each {
                object."removeFrom$cap"(it)
            }
        }

        def toAdd = domains.findAll{! (it in object."${internalAttrs.propertyName}")}

        toAdd.each{
            object."addTo$cap"(it)
        }
    }


}

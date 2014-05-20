package net.kaleidos.plugins.admin.widget.relation

import groovy.xml.MarkupBuilder

class RelationTableWidget extends AbstractRelationPopup {
    @Override
    String doRenderWithParent(String uuid, Closure parent) {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def options = [:]

        if (internalAttrs["relatedDomainClass"]) {
            def domainClass = internalAttrs["relatedDomainClass"].clazz
            def otherSideProperty = internalAttrs["grailsDomainClass"].getPropertyByName(internalAttrs['propertyName']).getOtherSide()

            def listUrl = grailsLinkGenerator.link(mapping: 'grailsAdminApiAction', params:[ 'slug': domainClass.simpleName.toLowerCase() ])

            value.each {id ->
                def element = domainClass.get(id)
                options[id] = element.toString()
            }

            builder.div class:"relationtablewidget clearfix", {
                options.each { key, value ->
                    input type: "hidden", name:htmlAttrs['name'], value: key
                }
                _elementsTable(delegate, domainClass, options, otherSideProperty.isOptional())
                div {
                    a class:"btn btn-default js-relationtablewidget-add", "data-url": listUrl, "data-toggle":"modal", "data-target":"#add-$uuid", href:"#", {
                        span class:"glyphicon glyphicon-plus", {
                            mkp.yield "Add"
                        }
                    }
                }
                parent(delegate)
            }
        }

        return writer.toString()
    }

    def _elementsTable(builder, domainClass, options, isOptional) {
        def detailUrl = grailsLinkGenerator.link(mapping: 'grailsAdminEdit', params:['slug':domainClass.simpleName.toLowerCase(), 'id':0])

        builder.table "data-detailurl":detailUrl, "data-property-name":internalAttrs["propertyName"], "data-optional":isOptional, class:"table table-bordered elements-table", {
            // We need an empty elemento so grails doesn't construct <table/> when there is no elements (it's not valid html)
            mkp.yield ""
            options.each { key, value ->
                def url = grailsLinkGenerator.link(mapping: 'grailsAdminEdit', params:['slug':domainClass.simpleName.toLowerCase(), 'id':key])
                tr {
                    td {
                        a href: url, { mkp.yield value }
                    }

                    if (isOptional) {
                        td class: "list-actions", {
                            a class: "btn btn-default btn-sm js-relationtablewidget-delete", "data-value":key, "href":"#", {
                                span class:"glyphicon glyphicon-trash" {
                                    mkp.yield "Delete"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

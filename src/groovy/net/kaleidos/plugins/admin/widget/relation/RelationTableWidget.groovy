package net.kaleidos.plugins.admin.widget.relation

import net.kaleidos.plugins.admin.widget.Widget
import groovy.xml.MarkupBuilder

class RelationTableWidget extends Widget{

    def grailsLinkGenerator

    public RelationTableWidget() {
        def ctx = grails.util.Holders.applicationContext
        grailsLinkGenerator = ctx.grailsLinkGenerator
    }

    String render() {
        if (htmlAttrs.disallowRelationships) {
            return "<p>Disabled relationship due to be inside an embedded dialog</p>"
        }

        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def options = [:]

        if (internalAttrs["relatedDomainClass"]) {
            def domainClass = internalAttrs["relatedDomainClass"].clazz
            def otherSideProperty = internalAttrs["grailsDomainClass"].getPropertyByName(internalAttrs['propertyName']).getOtherSide()
            def optional = otherSideProperty?otherSideProperty.isOptional():true

            def listUrl = grailsLinkGenerator.link(mapping: 'grailsAdminApiAction', params:[ 'slug': DomainInspector.getSlug(domainClass) ])

            value.each {id ->
                def element = domainClass.get(id)
                options[id] = element.toString()
            }

            builder.div class:"relationtablewidget clearfix", view:"relationtablewidget", {
                options.each { key, value ->
                    input type: "hidden", name:htmlAttrs['name'], value: key
                }
                _elementsTable(delegate, domainClass, options, optional)
                div {
                    a class:"btn btn-default js-relationtablewidget-add", "data-url": listUrl, href:"#", {
                        span class:"glyphicon glyphicon-plus", {
                            mkp.yield "Add"
                        }
                    }
                }
            }
        }

        return writer.toString()
    }

    def _elementsTable(builder, domainClass, options, isOptional) {
        def detailUrl = grailsLinkGenerator.link(mapping: 'grailsAdminEdit', params:['slug':DomainInspector.getSlug(domainClass), 'id':0])

        builder.table "data-detailurl":detailUrl, "data-property-name":internalAttrs["propertyName"], "data-optional":isOptional, class:"table table-bordered elements-table", {
            // We need an empty elemento so grails doesn't construct <table/> when there is no elements (it's not valid html)
            mkp.yield ""
            options.each { key, value ->
                def url = grailsLinkGenerator.link(mapping: 'grailsAdminEdit', params:['slug':DomainInspector.getSlug(domainClass), 'id':key])
                tr {
                    td {
                        a href: url, { mkp.yield value }
                    }

                    if (isOptional) {
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

    List<String> getAssets() {
        [ 'js/admin/relationpopup.js',
          'js/admin/relationtablewidget.js'
        ]
    }

    def getValueForJson() {
        def values = []
        def domainClass = internalAttrs["relatedDomainClass"].clazz
        value.each {id ->
            def element = domainClass.get(id)
            values << element.toString()
        }
        return values
    }
}

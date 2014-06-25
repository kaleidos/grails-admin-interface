package net.kaleidos.plugins.admin.widget

import groovy.xml.MarkupBuilder

class MapWidget extends Widget {

    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        def attrs = htmlAttrs.clone()

        attrs << ["type": "text"]
        attrs << ["value": value]
        attrs << ["class": "form-control map-widget-text js-map-widget-text"]

        builder.div(class: 'map-widget', view: "mapwidget") {
            div {
                span(class:'map-container') {
                    def iframeOptions = [:]
                    iframeOptions << ["width": 425]
                    iframeOptions << ["height": 350]
                    iframeOptions << ["frameborder": 0]
                    iframeOptions << ["scrolling": "no"]
                    iframeOptions << ["marginheight": 0]
                    iframeOptions << ["marginwidth": 0]
                    iframeOptions << ["src": "https://maps.google.com/maps?f=q&q=${value}&output=embed"]

                    iframe(iframeOptions, "")
                }
                input(type:'button', class:'map-widget-refresh js-map-widget-refresh', value:'Refresh')
            }
            div {
                input(attrs)
            }
        }
        return writer
    }

    List getAssets() {
        return [
            [ plugin:"admin-interface", file: "grails-admin/css/widgets/mapwidget.css" ],
            [ plugin:"admin-interface", file: "grails-admin/js/widgets/mapwidget.js" ],
        ]
    }
}

package net.kaleidos.plugins.admin.widget
import groovy.xml.MarkupBuilder

class MapWidget extends Widget {

    String render() {
        def html = new StringBuilder()
        html.append("<div class='map-widget' view='mapwidget'>")
        html.append("<div>")
        html.append("<span class='map-container'>")
        html.append("<iframe width='425' height='350' frameborder='0' scrolling='no' marginheight='0' marginwidth='0'")
        html.append("src='https://maps.google.com/maps?f=q&amp;q=${value}&amp;output=embed'")
        html.append("></iframe>")
        html.append("</span>")
        html.append("<input type='button' class='map-widget-refresh js-map-widget-refresh' value='Refresh' />")
        html.append("</div>")

        html.append("<div>")

        html.append("<input type='text' class='form-control map-widget-text js-map-widget-text' ")
        htmlAttrs.each{key, value ->
            html.append(" ${key.encodeAsHTML()}=\"${value.encodeAsHTML()}\"")
        }
        html.append("value='${(value!=null)?value.encodeAsHTML():''}' />")
        html.append("</div>")
        html.append("</div>")
        return html
    }

    List getAssets() {
        return [
            [ plugin:"admin-interface", file: "grails-admin/css/widgets/mapwidget.css" ],
            [ plugin:"admin-interface", file: "grails-admin/js/widgets/mapwidget.js" ],
        ]
    }
}

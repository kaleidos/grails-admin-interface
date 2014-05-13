package net.kaleidos.plugins.admin.widget.relation
import net.kaleidos.plugins.admin.widget.Widget
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import net.kaleidos.plugins.admin.GrailsAdminPluginTagLib

class RelationTableWidget extends Widget{
    def grailsAdminPluginTagLib

    @Override
    String render() {

        StringBuilder html = new StringBuilder()
        def options = [:]
        if (internalAttrs["relatedDomainClass"]) {
            def domainClass = internalAttrs["relatedDomainClass"].clazz
            value.each {id ->
                def element = domainClass.get(id)
                options[id] = element.toString()
            }

            html.append("<table class=\"table table-bordered\">")
            options.each{key, value ->

                def url = new ApplicationTagLib().createLink(mapping: 'grailsAdminEdit', params:['slug':domainClass.simpleName.toLowerCase(), 'id':key])
                def deleteUrl = new ApplicationTagLib().createLink(
                        mapping: 'grailsAdminDeleteRelatedApiAction',
                        params:[
                            'slug': internalAttrs["domainObject"].getClass().simpleName.toLowerCase(),
                            'id': internalAttrs["domainObject"].id,
                            'propertyName': internalAttrs["propertyName"],
                            'id2':key
                        ])

                html.append("<tr>")
                html.append("<td><a href=\"$url\">$value</a></td>")
                html.append("<td class=\"list-actions\">")
                html.append("<a class=\"btn btn-default btn-sm js-relationtablewidget-delete\" data-method=\"DELETE\" data-url=\"$deleteUrl\" href=\"#\"><span class=\"glyphicon glyphicon-trash\"></span> Delete</a></td>")
                html.append("</tr>")

                }
            html.append("</table>")
        }
        return html


    }
}

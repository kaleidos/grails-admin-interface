package net.kaleidos.plugins.admin.widget.relation
import net.kaleidos.plugins.admin.widget.Widget
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import grails.util.Holders
import java.util.UUID

class RelationTableWidget extends Widget{

    @Override
    String render() {

        StringBuilder html = new StringBuilder()
        def options = [:]
        def uuid =  UUID.randomUUID().toString()
        if (internalAttrs["relatedDomainClass"]) {
            def domainClass = internalAttrs["relatedDomainClass"].clazz
            def otherSideProperty = internalAttrs["grailsDomainClass"].getPropertyByName(internalAttrs['propertyName']).getOtherSide()
            value.each {id ->
                def element = domainClass.get(id)
                options[id] = element.toString()
            }
            html.append("<div class=\"relationtablewidget clearfix\">")
            options.each{key, value ->
                html.append("<input type=\"hidden\" name=\"${htmlAttrs['name']}\" value=\"${key}\" />")
            }

            def detailUrl = Holders.applicationContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib').createLink(mapping: 'grailsAdminEdit', params:['slug':domainClass.simpleName.toLowerCase(), 'id':0])


            html.append("<table data-detailurl=\"$detailUrl\" data-property-name=\"${internalAttrs["propertyName"]}\" data-optional=\"${otherSideProperty.isOptional()}\"class=\"table table-bordered\">")
            options.each{key, value ->

                def url = Holders.applicationContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib').createLink(mapping: 'grailsAdminEdit', params:['slug':domainClass.simpleName.toLowerCase(), 'id':key])

                html.append("<tr>")
                html.append("<td><a href=\"$url\">$value</a></td>")

                if (otherSideProperty.isOptional()) {
                    html.append("<td class=\"list-actions\">")
                    html.append("<a class=\"btn btn-default btn-sm js-relationtablewidget-delete\" data-value=\"$key\" href=\"#\"><span class=\"glyphicon glyphicon-trash\"></span> Delete</a></td>")
                    html.append("</tr>")
                }



                }
            html.append("</table>")


            def listUrl = Holders.applicationContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib').createLink(
                            mapping: 'grailsAdminApiAction',
                            params:[
                                'slug': domainClass.simpleName.toLowerCase(),
                            ])


            html.append("""
                <div>
                    <a class="btn btn-default js-relationtablewidget-add" data-url="$listUrl" data-toggle="modal" data-target="#add-$uuid" href="#">
                        <span class="glyphicon glyphicon-plus"></span> Add
                    </a>
                </div>
            </div>
            <div id="add-$uuid" tabindex="-1" role="dialog" aria-labelledby="confirmLabel" aria-hidden="true" class="modal fade">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" data-dismiss="modal" aria-hidden="true" class="close">×</button>
                                <h4 id="confirmLabel" class="modal-title">Add ${internalAttrs["propertyName"]}</h4>
                            </div>
                            <div class="modal-body">
                                Loading...
                            </div>
                            <div class="modal-footer">
                                <button type="button" data-dismiss="modal" class="btn btn-default">Close</button>
                                <button type="button" data-dismiss="modal" class="btn btn-plus js-relationtablewidget-add-action">Save</button>
                            </div>
                        </div>
                    </div>
            </div>
            """)

        }

        return html


    }
}

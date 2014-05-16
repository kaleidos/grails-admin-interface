package net.kaleidos.plugins.admin.widget.relation

import net.kaleidos.plugins.admin.widget.Widget
import groovy.xml.MarkupBuilder

class RelationPopupOneWidget extends Widget {
    def grailsLinkGenerator
    def adminConfigHolder

    public RelationPopupOneWidget() {
        def ctx = grails.util.Holders.applicationContext
        grailsLinkGenerator = ctx.grailsLinkGenerator
        adminConfigHolder = ctx.adminConfigHolder
    }

    @Override
    String render() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def relationObject = internalAttrs.domainObject?."${internalAttrs.propertyName}"
        def slug = adminConfigHolder.getDomainConfigForProperty(internalAttrs.domainClass, internalAttrs.propertyName).slug
        def action = grailsLinkGenerator.link(mapping:"grailsAdminApiAction", method: "put", params:[slug:slug])

        builder.div class:"relation-popupone-widget ", view:"relationpopuponewidget", "data-method":"put", action:action, {
            input type:'hidden', class:'js-one-rel-value', name:"${internalAttrs.propertyName}"
            a href:"/show", class:'js-one-rel-text', {
                if (value) {
                    mkp.yield "${relationObject}".encodeAsHTML()
                } else {
                    mkp.yield "<< empty >>"
                }
            }
            div class:"btn-group", {
                a href:"/list", class:"btn btn-default js-relationpopuponewidget-list", {
                    span class:"glyphicon glyphicon-list", {
                        mkp.yield "List"
                    }
                }
                a href:"/new", class:"btn btn-default js-relationpopuponewidget-new", {
                    span class:"glyphicon glyphicon-plus", {
                        mkp.yield "New"
                    }
                }
                a href:"/delete", class:"btn btn-default js-relationpopuponewidget-delete", {
                    span class:"glyphicon glyphicon-trash", {
                        mkp.yield "Delete"
                    }
                }
            }
        }


        /*
        builder.div(class: "field-container", {
            span(class: "field-actions",{
                span {
                    span {
                        a (href:"/admin/test", target: "new", {
                            mkp.yield "johndoe"
                        })
                    }
                    span(class:"btn-group", {
                        a (href:"/list", class:"btn btn-small",{
                            i (class:"icon-list", {})
                            mkp.yield "List"
                        })
                        a (href:"/new", class:"btn btn-small",{
                            i class:"icon-plus"
                            //mkp.yield "Add new"
                        })
                    })
                    span(class:"btn-group", {
                        a (href:"/delete", class:"btn btn-small",{
                            i class:"icon-off"
                            //mkp.yield "Delete"
                        })
                    })
                }
            })
        })
        */

        return writer.toString()
        /*
        <div class="field-container">
            <span class="field-actions">
                <span class="field-short-description">
                    <span class="inner-field-short-description">
                        <a href="/admin/sonata/user/user/45/edit" target="new">johndoe</a>
                    </span>
                </span>
                <span class="btn-group">
                    <a href="/admin/sonata/user/user/list?uniqid=s5375bfa9a51ad&amp;code=sonata.user.admin.user&amp;pcode=sonata.news.admin.post&amp;puniqid=s5375bfa98b90b" class="btn btn-small sonata-ba-action" title="List">
                        <i class="icon-list"></i>List
                    </a>
                    <a href="/admin/sonata/user/user/create?uniqid=s5375bfa9a51ad&amp;code=sonata.user.admin.user&amp;pcode=sonata.news.admin.post&amp;puniqid=s5375bfa98b90b" class="btn btn-small sonata-ba-action" title="Add new">
                        <i class="icon-plus"></i>Add new
                    </a>
                </span>
                <span class="btn-group">
                    <a href="" class="btn btn-small sonata-ba-action" title="Delete">
                        <i class="icon-off"></i>Delete
                    </a>
                </span>
            </span>
            <span style="display: none">
                <div class="col-lg-9 col-lg-offset-3">
                    <input type="text" name="s5375bfa98b90b[author]" required="required" class="span5 form-control" value="45">
                </div>
            </span>
            <div class="container sonata-ba-modal sonata-ba-modal-edit-one-to-one" style="display: none">
            </div>
        </div>
        */
    }
}

<%@ page defaultCodec="HTML" %>

<!DOCTYPE html>
<html>
    <head>
        <title>${domain.className} - <g:message code="grailsAdminPlugin.list.title" /></title>
        <meta name="layout" content="grailsAdmin/main" />
    </head>
    <body>
        <div class="main-container container">
            <table class="table table-bordered">
                <thead>
                    <gap:listTitles className="${domain.domainClass.clazz.name}" />
                    <th class="list-actions-head">
                        <g:message code="grailsAdminPlugin.list.actions" />
                    </th>
                </thead>
                <tbody>
                <g:each in="${objs}">
                    <tr>
                        <gap:listLine object="${it}" />
                        <td class="list-actions">
                            <g:link mapping="edit" params="[slug: domain.slug, id: it.id]" class="btn btn-default btn-sm">
                                <span class="glyphicon glyphicon-pencil"></span> <g:message code="grailsAdminPlugin.action.edit" />
                            </g:link>
                            <a data-id="${it.id}" data-toggle="modal" data-target="#confirm" class="btn btn-default btn-sm">
                                <span class="glyphicon glyphicon-trash"></span> <g:message code="grailsAdminPlugin.action.delete" />
                            </a>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>

            <g:if test="${totalPages > 1}">
            <ul class="pagination">
                <g:if test="${currentPage > 1}">
                <li>
                    <g:link mapping="list" params="[slug: domain.slug, page: currentPage - 1]">«</g:link>
                </li>
                </g:if>

                <g:each var="page" in="${ (1..totalPages) }">
                <li <g:if test="${page == currentPage}">class="active"</g:if>>
                    <g:link mapping="list" params="[slug: domain.slug, page: page]">${page}</g:link>
                </li>
                </g:each>

                <g:if test="${currentPage < totalPages}">
                <li>
                    <g:link mapping="list" params="[slug: domain.slug, page: currentPage + 1]">»</g:link>
                </li>
                </g:if>
            </ul>
            </g:if>
        </div>

        <div id="confirm" tabindex="-1" role="dialog" aria-labelledby="confirmLabel" aria-hidden="true" class="modal fade">
            <g:form mapping="delete" params="[slug: domain.slug]">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" data-dismiss="modal" aria-hidden="true" class="close">×</button>
                            <h4 id="confirmLabel" class="modal-title"><g:message code="grailsAdminPlugin.confirm.delete.title" /></h4>
                        </div>
                        <div class="modal-body"><g:message code="grailsAdminPlugin.confirm.delete.body" /></div>
                        <div class="modal-footer">
                            <button type="button" data-dismiss="modal" class="btn btn-default"><g:message code="grailsAdminPlugin.action.close" /></button>
                            <button type="submit" class="btn btn-danger"><g:message code="grailsAdminPlugin.action.delete" /></button>
                        </div>
                    </div>
                </div>
                <input type="hidden" name="id" value="" />
            </g:form>
        </div>
    </body>
</html>

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
                        Actions
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
                            <a class="btn btn-default btn-sm" href="edit.html">
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
    </body>
</html>

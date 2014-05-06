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
                <tbody>
                <g:each in="${objs}">
                    <tr>
                        <gap:listLine object="${it}" />
                        <td class="list-actions">
                            <a class="btn btn-default btn-sm" href="edit.html">
                                <span class="glyphicon glyphicon-pencil"></span> <g:message code="grailsAdminPlugin.action.edit" />
                            </a>
                            <a class="btn btn-default btn-sm" href="edit.html">
                                <span class="glyphicon glyphicon-trash"></span> <g:message code="grailsAdminPlugin.action.delete" />
                            </a>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </body>
</html>

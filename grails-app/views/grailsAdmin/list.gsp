<%@ page defaultCodec="HTML" %>

<!DOCTYPE html>
<html>
    <head>
        <title>${domain.className} - <g:message code="grailsAdminPlugin.list.title" /></title>
        <meta name="layout" content="grailsAdmin/main" />
    </head>
    <body>
        <div class="main-container container">
            <div class="row">
                <div class="col-md-7">
                    <ol class="breadcrumb">
                        <li><g:link mapping="grailsAdminDashboard"><g:message code='grailsAdminPlugin.dashboard.title'/></g:link></li>
                        <li class="active">${domain.className}</li>
                    </ol>
                </div>
                <div class="col-md-3 col-md-offset-2 object-nav">
                    <div class="btn-group">
                        <g:link mapping="grailsAdminAdd" params="[slug: domain.slug]" class="btn btn-default">
                            <span class="glyphicon glyphicon-plus"></span> <g:message code='grailsAdminPlugin.add.title' />
                        </g:link>
                    </div>
                </div>
            </div>
            <div class="table-container">
                <table class="table table-bordered">
                    <thead>
                        <th class="list-actions-head">
                            <g:message code="grailsAdminPlugin.list.actions" />
                        </th>
                        <gap:listTitles className="${domain.classFullName}" sort="${sort}" sortOrder="${sortOrder}" />
                        <th class="list-actions-head">
                            <g:message code="grailsAdminPlugin.list.actions" />
                        </th>
                    </thead>
                    <tbody>
                    <g:each in="${objs}">
                        <tr>
                            <gap:listLineActions object="${it}" />
                            <gap:listLine className="${domain.classFullName}" object="${it}" />
                            <gap:listLineActions object="${it}" />
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>

            <gap:pagination domain="${domain}" totalPages="${totalPages}" currentPage="${currentPage}"/>
        </div>
        <g:render plugin="grailsAdmin" template="/grailsAdmin/includes/delete" />
    </body>
</html>

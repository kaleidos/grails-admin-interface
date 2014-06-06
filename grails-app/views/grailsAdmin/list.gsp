<%@ page defaultCodec="HTML" %>

<!DOCTYPE html>
<html>
    <head>
        <title>${domain.className} - <g:message code="grailsAdminPlugin.list.title" /></title>
        <meta name="layout" content="grailsAdmin/main" />
    </head>
    <body>
        <div class="main-container container" view="listView">
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
                        <a data-url="${createLink(mapping: 'grailsAdminApiAction', params: [slug: domain.slug, id: 0])}"
                            data-toggle="modal" data-target="#confirm" class="btn btn-default">
                            <span class="glyphicon glyphicon-trash"></span> <g:message code="grailsAdminPlugin.action.delete" />
                        </a>
                    </div>
                </div>
            </div>
            <div class="table-container grails-admin-list-table">
                <table class="table table-bordered">
                    <thead>
                        <th class="list-select-head">
                            &nbsp;
                        </th>
                        <gap:listTitles className="${domain.classFullName}" sort="${sort}" sortOrder="${sortOrder}" />
                    </thead>
                    <tbody>
                    <g:each in="${objs}">
                        <gap:listLine className="${domain.classFullName}" object="${it}" />
                    </g:each>
                    </tbody>
                </table>
            </div>

            <gap:pagination domain="${domain}" totalPages="${totalPages}" currentPage="${currentPage}"/>
        </div>
        <g:render plugin="grailsAdmin" template="/grailsAdmin/includes/delete" />
    </body>
</html>

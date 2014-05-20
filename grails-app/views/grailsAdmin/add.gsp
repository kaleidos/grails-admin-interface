<%@ page defaultCodec="HTML" %>

<!DOCTYPE html>
<html>
    <head>
        <title>${domain.className} - <g:message code="grailsAdminPlugin.add.title" /></title>
        <meta name="layout" content="grailsAdmin/main" />
    </head>
    <body>
        <div class="main-container container">
            <div class="row">
                <div class="col-md-7">
                    <ol class="breadcrumb">
                        <li><g:link mapping="grailsAdminDashboard"><g:message code='grailsAdminPlugin.dashboard.title'/></g:link></li>
                        <li><g:link mapping="grailsAdminList" params="[slug: domain.slug]">${domain.className}</g:link></li>
                        <li class="active"><g:message code="grailsAdminPlugin.add.title" /></li>
                    </ol>
                </div>
                <div class="col-md-3 col-md-offset-2 object-nav">
                    <div class="btn-group">
                        <g:link mapping="grailsAdminList" params="[slug: domain.slug]" class="btn btn-default">
                            <span class="glyphicon glyphicon-list"></span> <g:message code='grailsAdminPlugin.action.return'/>
                        </g:link>
                    </div>
                </div>
            </div>

            <g:render template="/grailsAdmin/addForm" model="[domain: domain]" />
        </div>
    </body>
</html>

<%@ page defaultCodec="HTML" %>

<!DOCTYPE html>
<html>
    <head>
        <title><g:message code="grailsAdminPlugin.dashboard.title" /></title>
        <meta name="layout" content="grailsAdmin/main" />
    </head>
    <body>
        <div class="main-container container">
            <table class="table table-condensed dashboard">            
                <g:each var="domainClass" in="${domainClasses}">
                <tr>
                    <td>${domainClass.className}</td>
                    <td>
                        <div class="btn-group">
                            <g:link class="btn btn-link" mapping="list" params="[slug: domainClass.slug]">
                                <span class="glyphicon glyphicon-list"></span>
                                <g:message code="grailsAdminPlugin.dashboard.list" />
                            </g:link>
                            <g:link class="btn btn-link" mapping="add" params="[slug: domainClass.slug]">
                                <span class="glyphicon glyphicon-plus"></span>
                                <g:message code="grailsAdminPlugin.dashboard.add" />
                            </g:link>
                        </div>
                    </td>
                </tr>
                </g:each>
            </table>
        </div>
    </body>
</html>

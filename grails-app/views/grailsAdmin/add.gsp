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

            <g:form class="main-form" grailsadmin-remote="enabled" mapping="grailsAdminAdd" params="[slug: domain.slug]">
                <gap:createFormFields
                    className="${domain.classFullName}"

                    createWidgetProperties="${[
                        'class':'form-control'
                    ]}"

                    />

                <div class="form-options well">
                    <div class="btn-group">
                      <input type="button" value="${message(code: 'grailsAdminPlugin.form.action.save')}" class="btn btn-success form-action">
                    </div>
                    <div class="btn-group">
                      <input type="button" value="${message(code: 'grailsAdminPlugin.form.action.save.return')}" class="btn btn-default form-action" data-url="${createLink(mapping: 'list', params: [slug: domain.slug])}" >
                    </div>
                    <div class="btn-group">
                      <input type="button" value="${message(code: 'grailsAdminPlugin.form.action.save.add')}" class="btn btn-default form-action" data-url="${createLink(mapping: 'add', params: [slug: domain.slug])}" >
                    </div>
                </div>
            </g:form>


        </div>
    </body>
</html>

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
                        <li><g:link mapping="dashboard"><g:message code='grailsAdminPlugin.dashboard.title'/></g:link></li>
                        <li><g:link mapping="list" params="[slug: domain.slug]">${domain.className}</g:link></li>
                        <li class="active"><g:message code="grailsAdminPlugin.add.title" /></li>
                    </ol>
                </div>
                <div class="col-md-3 col-md-offset-2 object-nav">
                    <div class="btn-group">
                        <g:link mapping="list" params="[slug: domain.slug]" class="btn btn-default">
                            <span class="glyphicon glyphicon-list"></span> <g:message code='grailsAdminPlugin.action.return'/>
                        </g:link>
                    </div>
                </div>
            </div>

            <g:form class="main-form" mapping="add" params="[slug: domain.slug]">
                <gap:createFormFields
                    className="${domain.classFullName}"

                    createWidgetProperties="${[
                        'class':'form-control'
                    ]}"

                    />

                <div class="form-options well">
                    <div class="btn-group">
                      <input type="submit" name="save" value="Save" class="btn btn-success">
                    </div>
                    <div class="btn-group">
                      <input type="submit" name="saveAndReturn" value="Save and return list" class="btn btn-default">
                    </div>
                </div>
            </g:form>


        </div>
    </body>
</html>

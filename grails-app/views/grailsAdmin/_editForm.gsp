<%@ page defaultCodec="HTML" %>

<gap:widgetBeforeForm className="${domain.classFullName}" disallowRelationships="${embedded}"/>

<g:form view="formView" data-method="POST" data-url="${createLink(mapping: 'grailsAdminSuccess', params: [slug: domain.slug])}" class="validate-form main-form" grailsadmin-remote="enabled" mapping="grailsAdminApiAction" params="[slug: domain.slug, id:object.id]">
    <gap:editFormFields
        object="${object}"
        editWidgetProperties="${[ 'class':'form-control' ]}"/>

    <div class="form-options well">
        <div class="btn-group">
          <input type="button" data-url="${createLink(mapping: 'grailsAdminSuccessEdit', params: [slug: domain.slug])}" value="${message(code: 'grailsAdminPlugin.form.action.update')}" class="btn btn-primary form-action">
        </div>
        <div class="btn-group">
          <input type="button" data-url="${createLink(mapping: 'grailsAdminSuccessList', params: [slug: domain.slug])}" value="${message(code: 'grailsAdminPlugin.form.action.update.close')}" class="btn btn-default form-action">
        </div>
        <div class="btn-group">
            <a data-url="${createLink(mapping: 'grailsAdminApiAction', params: [slug: domain.slug, id: object.id])}" data-toggle="modal" data-target="#confirm" class="btn btn-danger">
                <g:message code="grailsAdminPlugin.action.delete" />
            </a>
        </div>
    </div>
</g:form>

<gap:widgetAfterForm
    className="${domain.classFullName}"
    disallowRelationships="${embedded}"/>

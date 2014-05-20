<%@ page defaultCodec="HTML" %>

<g:form view="formView" data-method="PUT" class="validate-form main-form" grailsadmin-remote="enabled" mapping="grailsAdminApiAction" params="[slug: domain.slug]">
    <gap:createFormFields
        className="${domain.classFullName}"
        createWidgetProperties="${[ 'class':'form-control' ]}"
        disallowRelationships="${embedded}"/>

    <g:if test="${!embedded}">
        <div class="form-options well">
            <div class="btn-group">
              <input type="button" data-url="${createLink(mapping: 'grailsAdminSuccessEdit', params: [slug: domain.slug])}" value="${message(code: 'grailsAdminPlugin.form.action.save')}" class="btn btn-success form-action">
            </div>
            <div class="btn-group">
              <input type="button" value="${message(code: 'grailsAdminPlugin.form.action.save.return')}" class="btn btn-default form-action" data-url="${createLink(mapping: 'grailsAdminSuccessList', params: [slug: domain.slug])}" >
            </div>
            <div class="btn-group">
              <input type="button" value="${message(code: 'grailsAdminPlugin.form.action.save.add')}" class="btn btn-default form-action" data-url="${createLink(mapping: 'grailsAdminSuccessNew', params: [slug: domain.slug])}" >
            </div>
        </div>
    </g:if>
</g:form>

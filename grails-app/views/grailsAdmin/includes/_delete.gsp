<div id="confirm" view="deleteModal" tabindex="-1" role="dialog" aria-labelledby="confirmLabel" aria-hidden="true" class="modal fade">
    <form view="formView" action="" data-method="DELETE" grailsadmin-remote="enabled">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" data-dismiss="modal" aria-hidden="true" class="close">×</button>
                    <h4 id="confirmLabel" class="modal-title"><g:message code="grailsAdminPlugin.confirm.delete.title" /></h4>
                </div>
                <div class="modal-body"><g:message code="grailsAdminPlugin.confirm.delete.body" /></div>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn btn-default"><g:message code="grailsAdminPlugin.action.close" /></button>
                    <button data-url="${createLink(mapping: 'grailsAdminSuccessDelete', params: [slug: domain.slug])}" type="button" class="btn btn-danger form-action"><g:message code="grailsAdminPlugin.action.delete" /></button>
                </div>
            </div>
        </div>
    </form>
</div>

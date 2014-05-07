<div id="confirm" tabindex="-1" role="dialog" aria-labelledby="confirmLabel" aria-hidden="true" class="modal fade">
    <g:form mapping="delete" params="[slug: domain.slug]">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" data-dismiss="modal" aria-hidden="true" class="close">×</button>
                    <h4 id="confirmLabel" class="modal-title"><g:message code="grailsAdminPlugin.confirm.delete.title" /></h4>
                </div>
                <div class="modal-body"><g:message code="grailsAdminPlugin.confirm.delete.body" /></div>
                <div class="modal-footer">
                    <button type="button" data-dismiss="modal" class="btn btn-default"><g:message code="grailsAdminPlugin.action.close" /></button>
                    <button type="submit" class="btn btn-danger"><g:message code="grailsAdminPlugin.action.delete" /></button>
                </div>
            </div>
        </div>
        <input type="hidden" name="id" value="" />
    </g:form>
</div>

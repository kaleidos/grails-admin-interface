<%@ page defaultCodec="HTML" %>

<td class="list-actions">
    <g:link mapping="grailsAdminEdit" params="[slug: domainSlug, id: domainId]" class="btn btn-default btn-sm">
        <span class="glyphicon glyphicon-pencil"></span> <g:message code="grailsAdminPlugin.action.edit" />
    </g:link>
    <a data-url="${createLink(mapping: 'grailsAdminApiAction', params: [slug: domainSlug, id: domainId])}" data-toggle="modal" data-target="#confirm" class="btn btn-default btn-sm">
        <span class="glyphicon glyphicon-trash"></span> <g:message code="grailsAdminPlugin.action.delete" />
    </a>
</td>

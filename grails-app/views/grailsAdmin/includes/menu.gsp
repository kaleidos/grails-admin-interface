<nav role="navigation" class="navbar navbar-default navbar-fixed-top">
    <g:link mapping="grailsAdminDashboard" class="navbar-brand"><g:message code="grailsAdminPlugin.name" /></g:link>
    <div class="container-fluid">
        <!--
        <ul class="nav navbar-nav">
            <g:each var="domainClass" in="${domainClasses}">
                <li <g:if test="${domainClass.slug == slug}">class="active"</g:if>><g:link mapping="grailsAdminList" params="[slug: domainClass.slug]">${domainClass.className}</g:link></li>
            </g:each>
        </ul>
        -->

        <div class="nav-text logout">
            <sec:loggedInUserInfo field="username" />
        </div>
    </div>
</nav>

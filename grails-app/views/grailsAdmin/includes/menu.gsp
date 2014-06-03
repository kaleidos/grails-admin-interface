<nav role="navigation" class="navbar navbar-default navbar-fixed-top">
    <g:link mapping="grailsAdminDashboard" class="navbar-brand"><g:message code="grailsAdminPlugin.name" /></g:link>
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <g:each var="group" in="${config.groups}">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">${group} <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <g:each var="domainClass" in="${config.getGroup(group)}">
                            <li><g:link mapping="grailsAdminList" params="[slug: domainClass.slug]">${domainClass.className}</g:link></li>
                        </g:each>
                    </ul>
                </li>
            </g:each>
        </ul>

        <div class="nav-text logout">
            <sec:loggedInUserInfo field="username" />
        </div>
    </div>
</nav>

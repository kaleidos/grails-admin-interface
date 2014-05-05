<nav role="navigation" class="navbar navbar-default navbar-fixed-top">
    <g:link mapping="dashboard" class="navbar-brand"><g:message code="grailsAdminPlugin.name" /></g:link>
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <g:each var="domainClass" in="${domainClasses}">
                <!--  <li class="active"> --> 
                <li><g:link mapping="list" params="[slug: domainClass.slug]">${domainClass.className}</g:link></li>
            </g:each>
        </ul>

        <div class="nav-text logout">admin<a href="#">Logout</a></div>
    </div>
</nav>

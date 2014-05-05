<nav role="navigation" class="navbar navbar-default navbar-fixed-top">
    <a href="/" class="navbar-brand">Grails admin</a>
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

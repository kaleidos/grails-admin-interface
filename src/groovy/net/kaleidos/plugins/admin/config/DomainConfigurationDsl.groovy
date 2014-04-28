package net.kaleidos.plugins.admin.config

class DomainConfigurationDsl {
    def grailsApplication

    Closure closure
    Map domains = [:]

    public DomainConfigurationDsl(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.delegate = this
        this.closure = closure
    }

    public void execute() {
        this.closure()
    }

    def methodMissing(String name, args) {
        def domainClass = grailsApplication.domainClasses.find { it.fullName == name }
        if (!domainClass) {
            throw new RuntimeException("The class ${name} doesn't match with any domain class")
        }

        Map params = null
        if (args.length > 0 && args[0] instanceof Map) {
            params = (Map)args[0]
        }
        domains[name] = new DomainConfig(domainClass, params)
    }
}

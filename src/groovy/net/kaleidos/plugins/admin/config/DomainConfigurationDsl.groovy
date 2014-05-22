package net.kaleidos.plugins.admin.config

class DomainConfigurationDsl {
    Class clazz
    Closure closure
    Map params = [:]

    public DomainConfigurationDsl(Class clazz, Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.delegate = this
        this.closure = closure
        this.clazz = clazz
    }

    public DomainConfig execute() {
        this.params = [:]
        this.closure()
        return _buildDomainConfig(this.params)
    }

    private _buildDomainConfig(Map params) {
        def domainConfig = new DomainConfig(clazz)
        params.each { method, properties ->
            if (['list', 'create', 'edit'].contains(method)) {
                if (properties['excludes'] && properties['includes']) {
                    throw new RuntimeException("The includes and exludes configuration is setted for domain: ${domainClass.name}. Only one can be defined")
                }

                if (properties['excludes']) {
                    domainConfig.excludes[method] = properties['excludes']
                }

                if (properties['includes']) {
                    domainConfig.includes[method] = properties['includes']
                }

                if (properties['customWidgets']) {
                    domainConfig.customWidgets[method] = properties['customWidgets']
                }
            }
        }
        return domainConfig
    }

    def methodMissing(String name, args) {
        assert ['list', 'create', 'edit'].contains(name), "$name is not a valid property"
        assert args.size() == 1, "$args is not valid"
        assert args[0] instanceof Map, "${ args[0] } is not valid"

        this.params[name] = args[0]
    }
}

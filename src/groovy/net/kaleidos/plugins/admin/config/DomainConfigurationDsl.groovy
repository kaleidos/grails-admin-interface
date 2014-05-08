package net.kaleidos.plugins.admin.config

class DomainConfigurationDsl {
    def grailsApplication

    Closure closure
    Map params = [:]

    public DomainConfigurationDsl(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure.delegate = this
        this.closure = closure
    }

    public Map execute() {
        this.params = [:]
        this.closure()
        return this.params
    }

    def methodMissing(String name, args) {
        assert ['list', 'create', 'edit'].contains(name), "$name is not a valid property"
        assert args.size() == 1, "$args is not valid"
        assert args[0] instanceof Map, "${ args[0] } is not valid"

        this.params[name] = args[0]
    }
}

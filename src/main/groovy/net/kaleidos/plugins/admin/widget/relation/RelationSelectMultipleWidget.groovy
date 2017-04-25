package net.kaleidos.plugins.admin.widget.relation
import net.kaleidos.plugins.admin.widget.SelectMultipleWidget

class RelationSelectMultipleWidget extends SelectMultipleWidget {

    @Override
    String render() {
        def options = [:]
        if (internalAttrs["relatedDomainClass"]) {
            internalAttrs["relatedDomainClass"].list().each {
                options[it.id] = it.toString()
            }
        }
        internalAttrs.options = options


        return super.render()
    }

    public void updateValue() {
        def domains = []
        def object = internalAttrs["domainObject"]

        if (value) {
            if (value instanceof List) {
                value.each {
                    domains << internalAttrs['relatedDomainClass'].get(it as Long)
                }
            } else {
                domains << internalAttrs['relatedDomainClass'].get(value as Long)
            }
        }

        def cap = internalAttrs.propertyName.capitalize()
        if (object."${internalAttrs.propertyName}") {
            def current = []
            def toDelete = (object."${internalAttrs.propertyName}").findAll{! (it in domains)}
            current.addAll(toDelete)
            current.each {
                object."removeFrom$cap"(it)
            }
        }

        def toAdd = domains.findAll{! (it in object."${internalAttrs.propertyName}")}

        toAdd.each{
            object."addTo$cap"(it)
        }
    }
}

package net.kaleidos.plugins.admin.widget.relation
import net.kaleidos.plugins.admin.widget.SelectWidget

class RelationSelectWidget extends SelectWidget {

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
        if (value) {
            def object =  internalAttrs['relatedDomainClass'].get(value as Long)
            updateValue(object)
        } else {
            updateValue(null)
        }
    }
}

package net.kaleidos.plugins.admin.widget.relation
import net.kaleidos.plugins.admin.widget.SelectWidget

class RelationSelectWidget extends SelectWidget {

    @Override
    String render() {
        def options = [:]
        if (internalAttrs["relatedDomainClass"]) {
            internalAttrs["relatedDomainClass"].clazz.list().each {
                options[it.id] = it.toString()
            }
        }
        internalAttrs.options = options


        return super.render()
    }
}

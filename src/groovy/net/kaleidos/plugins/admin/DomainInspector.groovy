package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.codehaus.groovy.grails.validation.ConstrainedProperty
import grails.util.Holders
import org.springframework.util.ClassUtils

class DomainInspector {
    def object
    def domainClass

    static DomainInspector find(String name) {
        def domainClass = Holders.grailsApplication.domainClasses.find { it.clazz.name == name }
        if (!domainClass) {
            return null
        }
        return new DomainInspector(domainClass.clazz)
    }

    public DomainInspector(Object object) {
        def realClass = ClassUtils.getUserClass(object.getClass())
        this.domainClass = Holders.grailsApplication.domainClasses.find { it.clazz == realClass }
        this.object = object
    }

    public DomainInspector(Class clazz) {
        this.domainClass = Holders.grailsApplication.domainClasses.find { it.clazz == clazz }
    }

    public List<String> getPropertyNames(boolean persistent=true) {
        if (persistent) {
            return domainClass.getPersistentProperties().collect { it.name }
        } else {
            return domainClass.getProperties().collect { it.name }
        }
    }

    public GrailsDomainClassProperty _get(String name) {
        return domainClass.getPropertyByName(name)
    }

    public boolean isSortable(String property) {
        return !domainClass.getPropertyByName(property).isAssociation()
    }

    public boolean isAssociation(String property) {
        return domainClass.getPropertyByName(property).isAssociation()
    }

    public boolean isOneToMany(String property) {
        return domainClass.getPersistentProperty(property).isOneToMany()
    }

    public boolean isManyToMany(String property) {
        return domainClass.getPersistentProperty(property).isManyToMany()
    }

    public boolean isDomainClass(String property) {
        return Holders.grailsApplication.isDomainClass(_get(property).type)
    }

    public Class getPropertyClass(String property) {
        //return _get(property).getReferencedPropertyType()
        //return _get(property).getReferencedDomainClass().clazz
        return _get(property).type
    }

    public Class getPropertyDomainClass(String property) {
        return _get(property).getReferencedDomainClass().clazz
    }

    Collection getPropertyConstraints(propertyName) {
        ConstrainedProperty field = this.domainClass.constrainedProperties.get(propertyName)
        if (!field){
            return []
        }
        return field.getAppliedConstraints()
    }

    public String getClassName() {
        return domainClass.clazz.getName().tokenize(".")[-1]
    }

    public String getClassFullName() {
        return domainClass.clazz.getName()
    }

    public Class getClazz() {
        return this.domainClass.clazz
    }
}

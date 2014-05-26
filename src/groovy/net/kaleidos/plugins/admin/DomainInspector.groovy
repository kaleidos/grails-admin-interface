package net.kaleidos.plugins.admin

import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.codehaus.groovy.grails.validation.ConstrainedProperty
import grails.util.Holders
import org.springframework.util.ClassUtils
import org.codehaus.groovy.grails.exceptions.InvalidPropertyException

@groovy.util.logging.Log4j
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
        try {
            return domainClass.getPropertyByName(name)
        } catch(InvalidPropertyException e) {
            log.error e.message
            return null
        }
    }

    public boolean isSortable(String property) {
        def validProperties = getPropertyNames(true)
        return validProperties.contains(property) && !_get(property).isAssociation()
    }

    public boolean isAssociation(String property) {
        return domainClass.getPersistentProperty(property).isAssociation()
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
        def clazz = _get(property).type
        if (clazz.isPrimitive()) {
            clazz = org.apache.commons.lang.ClassUtils.primitiveToWrapper(clazz)
        }
        return clazz
    }

    public Class getPropertyDomainClass(String property) {
        def domain = _get(property).getReferencedDomainClass()?.clazz
        if (domain) {
            return domain
        }
        if (isDomainClass(property)) {
            return getPropertyClass(property)
        }
        return null
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

    public String getSlug() {
        return getSlug(this.domainClass.clazz)
    }

    public static String getSlug(Class clazz) {
        def slug = clazz.simpleName.toLowerCase()
    }
}

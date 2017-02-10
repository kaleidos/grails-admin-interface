package net.kaleidos.plugins.admin

import grails.util.Holders
import grails.core.GrailsDomainClassProperty
import org.grails.core.exceptions.InvalidPropertyException
import grails.gorm.validation.ConstrainedProperty
import org.springframework.util.ClassUtils

@groovy.util.logging.Log4j
class DomainInspector {
    def object
    def domainClass

    static Boolean isDomain(Class clazz) {
        return Holders.grailsApplication.domainClasses.find { it.clazz == clazz } != null
    }

    static DomainInspector find(String name) {
        def domainClass = Holders.grailsApplication.domainClasses.find { it.clazz.name == name }
        if (!domainClass) {
            return null
        }
        return new DomainInspector(domainClass.clazz)
    }

    static Class getClassWithSlug(String slug) {
        def domainClass = Holders.grailsApplication.domainClasses.find { getSlug(it.clazz) == slug }
        if (!domainClass) {
            return null
        }
        return domainClass.clazz
    }

    public DomainInspector(Object object) {
        def realClass = ClassUtils.getUserClass(object.getClass())
        this.domainClass = Holders.grailsApplication.domainClasses.find { it.clazz == realClass }
        this.object = object
        if (this.domainClass == null) {
            throw new RuntimeException("$domainClass is not a domain class")
        }
    }

    public DomainInspector(Class clazz) {
        this.domainClass = Holders.grailsApplication.domainClasses.find { it.clazz == clazz }
        if (this.domainClass == null) {
            throw new RuntimeException("$domainClass is not a domain class")
        }
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

    public boolean isOneToOne(String property) {
        return domainClass.getPersistentProperty(property).isOneToOne()
    }

    public boolean isManyToOne(String property) {
        return domainClass.getPersistentProperty(property).isManyToOne()
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

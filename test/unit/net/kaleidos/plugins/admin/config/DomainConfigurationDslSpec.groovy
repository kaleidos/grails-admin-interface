package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import spock.lang.Specification

class DomainConfigurationDslSpec extends Specification {
    void "Retrieve domain classes (excludes)"() {
        setup:
            def config = new DomainConfigurationDsl({
                list excludes: ['year']
                create excludes: ['year']
                edit excludes: ['year']
            })

        and: "Grails application"
            def grailsApplication = new DefaultGrailsApplication()
            grailsApplication.configureLoadedClasses([admin.test.TestOtherDomain.class] as Class[])
            config.grailsApplication = grailsApplication

        and: "Grails domain class"
            def grailsDomainClass = grailsApplication.domainClasses[0]

        when:
            def result = new DomainConfig(grailsDomainClass, config.execute())

        then:
            result.getExcludes('list') == ['year']
            result.getExcludes('create') == ['year']
            result.getExcludes('edit') == ['year']
    }

    void "Retrieve domain classes (includes)"() {
        setup:
            def config = new DomainConfigurationDsl({
                list includes: ['name', 'year']
                create includes: ['name', 'year']
                edit includes: ['name', 'year']
            })

        and: "Grails application"
            def grailsApplication = new DefaultGrailsApplication()
            grailsApplication.configureLoadedClasses([admin.test.TestOtherDomain.class] as Class[])
            config.grailsApplication = grailsApplication

        and: "Grails domain class"
            def grailsDomainClass = grailsApplication.domainClasses[0]

        when:
            def result = new DomainConfig(grailsDomainClass, config.execute())

        then:
            result.getIncludes('list') == ['name', 'year']
            result.getIncludes('create') == ['name', 'year']
            result.getIncludes('edit') == ['name', 'year']
    }
}

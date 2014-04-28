package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import spock.lang.Specification

class DomainConfigurationDslSpec extends Specification {
    void "Retrieve domain classes (excludes)"() {
        setup:
            def config = new DomainConfigurationDsl({
                "admin.test.TestDomain"()
                "admin.test.TestOtherDomain"(
                    list: [ excludes: ['year'] ],
                    show: [ excludes: ['year'] ],
                    edit: [ excludes: ['year'] ]
                )
            })

        and: "Grails application"
            def grailsApplication = new DefaultGrailsApplication()

            grailsApplication.configureLoadedClasses([
                admin.test.TestDomain.class,
                admin.test.TestOtherDomain.class] as Class[])

            config.grailsApplication = grailsApplication

        when:
            config.execute()

        then:
            config.domains.size() == 2
            config.domains["admin.test.TestOtherDomain"] != null
            config.domains["admin.test.TestOtherDomain"].domainClass != null
            config.domains["admin.test.TestOtherDomain"].domainClass.clazz == admin.test.TestOtherDomain.class
            config.domains["admin.test.TestOtherDomain"].getExcludes('list') == ['year']
            config.domains["admin.test.TestOtherDomain"].getExcludes('show') == ['year']
            config.domains["admin.test.TestOtherDomain"].getExcludes('edit') == ['year']

    }

    void "Retrieve domain classes (includes)"() {
        setup:
            def config = new DomainConfigurationDsl({
                "admin.test.TestDomain"()
                "admin.test.TestOtherDomain"(
                    list: [ includes: ['name', 'year'] ],
                    show: [ includes: ['name', 'year'] ],
                    edit: [ includes: ['name', 'year'] ]
                )
            })

        and: "Grails application"
            def grailsApplication = new DefaultGrailsApplication()

            grailsApplication.configureLoadedClasses([
                admin.test.TestDomain.class,
                admin.test.TestOtherDomain.class] as Class[])

            config.grailsApplication = grailsApplication

        when:
            config.execute()

        then:
            config.domains.size() == 2
            config.domains["admin.test.TestOtherDomain"] != null
            config.domains["admin.test.TestOtherDomain"].domainClass != null
            config.domains["admin.test.TestOtherDomain"].domainClass.clazz == admin.test.TestOtherDomain.class
            config.domains["admin.test.TestOtherDomain"].getIncludes('list') == ['name', 'year']
            config.domains["admin.test.TestOtherDomain"].getIncludes('show') == ['name', 'year']
            config.domains["admin.test.TestOtherDomain"].getIncludes('edit') == ['name', 'year']

    }

    void "Retrieve domain classes"() {
        setup:
            def config = new DomainConfigurationDsl({
                "NO_DEFINED"()
            })

        and: "Grails application"
            def grailsApplication = new DefaultGrailsApplication()

            grailsApplication.configureLoadedClasses([
                admin.test.TestDomain.class,
                admin.test.TestOtherDomain.class] as Class[])

            config.grailsApplication = grailsApplication

        when:
            config.execute()

        then:
            thrown(RuntimeException)

    }

}

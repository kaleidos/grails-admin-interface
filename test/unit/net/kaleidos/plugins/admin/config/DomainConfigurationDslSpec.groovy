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
                    create: [ excludes: ['year'] ],
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
            def result = config.domains["admin.test.TestOtherDomain"]

        then:
            config.domains.size() == 2
            result != null
            result.domainClass != null
            result.domainClass.clazz == admin.test.TestOtherDomain.class
            result.getExcludes('list') == ['year']
            result.getExcludes('create') == ['year']
            result.getExcludes('edit') == ['year']

    }

    void "Retrieve domain classes (includes)"() {
        setup:
            def config = new DomainConfigurationDsl({
                "admin.test.TestDomain"()
                "admin.test.TestOtherDomain"(
                    list: [ includes: ['name', 'year'] ],
                    create: [ includes: ['name', 'year'] ],
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
            def result = config.domains["admin.test.TestOtherDomain"]

        then:
            config.domains.size() == 2
            result != null
            result.domainClass != null
            result.domainClass.clazz == admin.test.TestOtherDomain.class
            result.getIncludes('list') == ['name', 'year']
            result.getIncludes('create') == ['name', 'year']
            result.getIncludes('edit') == ['name', 'year']

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

    void "Test config domain external admin"() {
        setup:
            def config = new DomainConfigurationDsl({
                "admin.test.TestConfigDomain"(
                    adminClass: "admin.test.TestConfigDomainAdmin"
                )
            })

        and: "Grails application"
            def grailsApplication = new DefaultGrailsApplication()
            grailsApplication.configureLoadedClasses([
                admin.test.TestConfigDomain.class] as Class[])
            config.grailsApplication = grailsApplication

        when:
            config.execute()
            def result = config.domains["admin.test.TestConfigDomain"]

        then:
            config.domains.size() == 1
            result != null
            result.domainClass != null
            result.domainClass.clazz == admin.test.TestConfigDomain.class
            result.getIncludes('list') == ['name', 'year']
            result.getIncludes('create') == ['name', 'year']
            result.getIncludes('edit') == ['name', 'year']

    }
}

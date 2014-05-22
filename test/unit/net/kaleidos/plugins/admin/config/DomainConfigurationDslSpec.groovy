package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import spock.lang.Specification

import grails.util.Holders

class DomainConfigurationDslSpec extends Specification {
    def setupSpec() {
        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            NameYearDomain.class,
        ] as Class[])
    }

    void "Retrieve domain classes (excludes)"() {
        setup:
            def config = new DomainConfigurationDsl(NameYearDomain.class, {
                list excludes: ['year']
                create excludes: ['year']
                edit excludes: ['year']
            })

        when:
            def result = config.execute()

        then:
            result.getExcludes('list') == ['year']
            result.getExcludes('create') == ['year']
            result.getExcludes('edit') == ['year']
    }

    void "Retrieve domain classes (includes)"() {
        setup:
            def config = new DomainConfigurationDsl(NameYearDomain.class, {
                list includes: ['name', 'year']
                create includes: ['name', 'year']
                edit includes: ['name', 'year']
            })

        when:
            def result = config.execute()

        then:
            result.getIncludes('list') == ['name', 'year']
            result.getIncludes('create') == ['name', 'year']
            result.getIncludes('edit') == ['name', 'year']
    }

    void "Customize widget for attribute"() {
        setup:
            def config = new DomainConfigurationDsl(NameYearDomain.class, {
                list customWidgets: ['name': 'test.MyWidget']
            })

        when:
            def result = config.execute()
            def widgetMap = result.getCustomWidgets("list")

        then:
            result != null
            result.getCustomWidgets("list") != null
            result.getCustomWidgets("list").name != null
    }

}


class NameYearDomain {
    transient testTransient
    Long id
    Long version
    String name
    String year
}

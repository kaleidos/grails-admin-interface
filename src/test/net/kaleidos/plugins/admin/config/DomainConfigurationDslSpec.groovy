package net.kaleidos.plugins.admin.config

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import spock.lang.*

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
                widget 'name', 'test.MyWidget'
            })

        when:
            def result = config.execute()
            def widgetMap = result.getCustomWidgets(formType)

        then:
            result != null
            widgetMap != null
            widgetMap.name != null

        where:
            formType << ['create', 'edit']
    }

    void "Customize widget for attribute. Additional params"() {
        setup:
            def config = new DomainConfigurationDsl(NameYearDomain.class, {
                widget 'name', 'test.MyWidget', dateFormat:'dd/MM/yyyy'
            })

        when:
            def result = config.execute()
            def widgetMap = result.getCustomWidgets(formType)

        then:
            result != null
            widgetMap != null
            widgetMap.name.class == "test.MyWidget"
            widgetMap.name.attributes != null
            widgetMap.name.attributes.dateFormat == 'dd/MM/yyyy'

        where:
            formType << ['create', 'edit']
    }

    void "Customize attribute groups"() {
        setup:
            def config = new DomainConfigurationDsl(NameYearDomain.class, {
                groups {
                    "First group" ( style:"collapse", fields: [ 'other', 'another', 'yetanother' ] )
                    "Second group" ( style:"tab", fields: [ 'year' ])
                }
            })
        when:
            def result = config.execute()
            def groupNames = result.getGroupNames()
            def defaultGroupFields = result.getDefinedPropertiesForGroup(method) // default

            def group1GroupFields = result.getDefinedPropertiesForGroup(method,"First group")
            def group1GroupStyle = result.getStylePropertiesForGroup("First group")

            def group2GroupFields = result.getDefinedPropertiesForGroup(method,"Second group")
            def group2GroupStyle = result.getStylePropertiesForGroup("Second group")

        then:
            result != null
            groupNames == ["First group", "Second group"]

            defaultGroupFields == ['name']
            group1GroupFields == ['other', 'another', 'yetanother']
            group2GroupFields == ['year']

            group1GroupStyle == "collapse"
            group2GroupStyle == "tab"

        where:
            method << ['create','edit']
    }
}


class NameYearDomain {
    transient testTransient
    Long id
    Long version
    String name
    String year
    String other
    String another
    String yetanother
}

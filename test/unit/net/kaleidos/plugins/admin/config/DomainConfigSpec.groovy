package net.kaleidos.plugins.admin.config

import spock.lang.Specification

import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass

class DomainConfigSpec extends Specification {
    void "Get properties"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [:])

        when:
            def result = domainConfig.getProperties("list")

        then:
            result.size() == 7
            result.containsAll(['id', 'version', 't1', 't2', 't3', 't4', 't5'] as Object[])
    }

    void "Get properties. Exclude some"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [list:[excludes:['t2', 't3']]])

        when:
            def result = domainConfig.getProperties("list")

        then:
            result.size() == 5
            result.containsAll(['id', 'version', 't1', 't4', 't5'] as Object[])
    }

    void "Get properties. Include some"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [list:[includes:['t2', 't3']]])

        when:
            def result = domainConfig.getProperties("list")

        then:
            result.size() == 2
            result.containsAll(['t2', 't3'] as Object[])
    }

    void "Get properties. Exception if includes and exludes"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])

        when:
            new DomainConfig(domainClass, [list:[excludes:['t2', 't3'], includes:['t2', 't3']]])

        then:
            thrown(RuntimeException)
    }
}

class Test {
    Long id
    Long version
    String t1
    String t2
    String t3
    String t4
    String t5
}

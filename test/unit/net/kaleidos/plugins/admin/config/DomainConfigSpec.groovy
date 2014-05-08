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
            result != null
            result == ['t1', 't2', 't3', 't4', 't5']
    }

    void "Get properties. Exclude some"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [list:[excludes:['t2', 't3']]])

        when:
            def result = domainConfig.getProperties("list")

        then:
            result.size() == 3
            result == ['t1', 't4', 't5']
    }

    void "Get properties. Include some"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [list:[includes:['t2', 't3']]])

        when:
            def result = domainConfig.getProperties("list")

        then:
            result.size() == 2
            result == ['t2', 't3']
    }

    void "Get properties. Include some. Respect my sort"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [list:[includes:['t5', 't4', 't3']]])

        when:
            def result = domainConfig.getProperties("list")

        then:
            result.size() == 3
            result == ['t5', 't4', 't3']
    }

    void "Get properties. Exception if includes and exludes"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])

        when:
            new DomainConfig(domainClass, [list:[excludes:['t2', 't3'], includes:['t2', 't3']]])

        then:
            thrown(RuntimeException)
    }

    def "Get short class names"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [:])

        when:
            def result = domainConfig.className

        then:
            domainConfig.className == "Test"
    }

    def "Get lower and short class names"(){
        setup:
            def domainClass = new DefaultGrailsDomainClass(Test.class, [:])
            def domainConfig = new DomainConfig(domainClass, [:])

        when:
            def result = domainConfig.slug

        then:
            result == "test"
    }
}

class Test {
    Long id
    Long version
    String t5
    String t3
    String t4
    String t1
    String t2
}

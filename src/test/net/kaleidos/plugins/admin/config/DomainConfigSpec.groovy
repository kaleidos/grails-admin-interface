package net.kaleidos.plugins.admin.config

import spock.lang.Specification
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication

import grails.util.Holders

class DomainConfigSpec extends Specification {
    def setupSpec() {
        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            Test.class,
            Test2.class,
            Test3.class,
        ] as Class[])
    }

    void "Get properties"(){
        setup:
            def domainConfig = new DomainConfig(Test.class)

        when:
            def result = domainConfig.getDefinedProperties("list")

        then:
            result != null
            result == ['t1', 't2', 't3', 't4', 't5']
    }

    void "Get properties on hierarchy"(){
        setup:
            def domainConfig = new DomainConfig(Test3.class)

        when:
            def result = domainConfig.getDefinedProperties("list")

        then:
            result != null
            result == ['other', 't1', 't2', 't3', 't4', 't5' ]
    }

    void "Get properties. Exclude some"(){
        setup:
            def domainConfig = new DomainConfig(Test.class)
            domainConfig.excludes['list'] = ['t2', 't3']

        when:
            def result = domainConfig.getDefinedProperties("list")

        then:
            result.size() == 3
            result == ['t1', 't4', 't5']
    }

    void "Get properties. Include some"(){
        setup:
            def domainConfig = new DomainConfig(Test.class)
            domainConfig.includes['list'] = ['t2', 't3']

        when:
            def result = domainConfig.getDefinedProperties("list")

        then:
            result.size() == 2
            result == ['t2', 't3']
    }

    void "Get properties. Include some. Respect my sort"(){
        setup:
            def domainConfig = new DomainConfig(Test.class)
            domainConfig.includes['list'] = ['t5', 't4', 't3']

        when:
            def result = domainConfig.getDefinedProperties("list")

        then:
            result.size() == 3
            result == ['t5', 't4', 't3']
    }

    void "Get sortable properties"(){
        setup:
            def domainConfig = new DomainConfig(Test2.class)

        when:
            def result = domainConfig.getSortableProperties("list")

        then:
            result.size() == 2
            result == ['t3', 't5']
    }


    def "Get short class names"(){
        setup:
            def domainConfig = new DomainConfig(Test.class)

        when:
            def result = domainConfig.className

        then:
            domainConfig.className == "Test"
    }

    def "Get lower and short class names"(){
        setup:
            def domainConfig = new DomainConfig(Test.class)

        when:
            def result = domainConfig.slug

        then:
            result == "test"
    }

}

class Test {
    transient testTransient
    Long id
    Long version
    String t5
    String t3
    String t4
    String t1
    String t2
}

class Test2 {
    transient testTransient
    Long id
    Long version
    Test test
    String t5
    String t3
}

class Test3 extends Test {
    String other
}

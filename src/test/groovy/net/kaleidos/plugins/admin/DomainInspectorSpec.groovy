package net.kaleidos.plugins.admin

import admin.test.TestDomain
import admin.test.TestOtherDomain
import grails.core.DefaultGrailsApplication
import grails.util.Holders
import org.grails.validation.NullableConstraint
import spock.lang.Specification
import spock.lang.Unroll

class DomainInspectorSpec extends Specification{
    def setupSpec() {
        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([ TestDomain ] as Class[])
    }

    void 'Find domain inspector by name'() {
        when:
            def inspector = DomainInspector.find("admin.test.TestDomain")
        then:
            inspector != null
            inspector.domainClass.clazz == TestDomain
    }

    @Unroll
    void 'Get property names #persistent'() {
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getPropertyNames(persistent)

        then:
            result.containsAll(properties)

        where:
            persistent << [true,false]
            properties << [
                ['name','year'],
                ['transientTest', 'name','year'],
            ]
    }

    @Unroll
    void 'Check whether is sortable #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.isSortable(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [false, true, true, false, false]
    }

    @Unroll
    void 'Check whether is association #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.isAssociation(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [false, false, false, true, true]
    }

    @Unroll
    void 'Check whether is 1-n #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.isOneToMany(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [false, false, false, true, false]
    }

    @Unroll
    void 'Check whether is 1 to many #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.isManyToMany(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [false, false, false, false, false]
    }

    @Unroll
    void 'Check whether is domain #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.isDomainClass(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [false, false, false, false, true]
    }

    @Unroll
    void 'retrieve property class #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getPropertyClass(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [Object, String, Integer, Set, TestOtherDomain]
    }

    @Unroll
    void 'Check whether is domain class #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getPropertyDomainClass(property)

        then:
            result == expectedResult

        where:
            property << ['transientTest', 'name','year', 'domain', 'otherDomain']
            expectedResult << [null, null, null, TestDomain, TestOtherDomain]
    }

    @Unroll
    void 'Check constraints for #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getPropertyConstraints(property)
            println "$result"

        then:
            result != null
            result.find { it.class == NullableConstraint } != null

        where:
            property << ['name','year', 'domain', 'otherDomain']
    }

    void 'Retrieve classname #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getClassName()

        then:
            result == "TestDomain"
    }

    void 'Retrieve full classname #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getClassFullName()

        then:
            result == "admin.test.TestDomain"
    }

    void 'Retrieve clazz #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getClazz()

        then:
            result == admin.test.TestDomain
    }

    void 'Retrieve slug #property'(){
        setup:
            def inspector = new DomainInspector(TestDomain)

        when:
            def result = inspector.getSlug()

        then:
            result == 'testdomain'
    }
}

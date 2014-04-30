package net.kaleidos.plugins.admin

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin

import spock.lang.Specification
import spock.lang.Unroll

import admin.test.TestDomain

@TestFor(GrailsAdminPluginGenericService)
@TestMixin(DomainClassUnitTestMixin)
class GrailsAdminPluginGenericServiceSpec extends Specification {
    void "List domains without filters"() {
        setup:
            mockDomain(TestDomain, [
                [name: movie1, year: year1],
                [name: movie2, year: year2],
                [name: movie3, year: year3] ])
        when:
            def result = service.listDomains(TestDomain)

        then:
            result != null
            result.size() == 3
            result.find { it.name == movie1 } != null
            result.find { it.name == movie2 } != null
            result.find { it.name == movie3 } != null

        where:
            movie1 = "Lord of the Rings"
            year1 = 2001

            movie2 = "Star Wars"
            year2 = 1977

            movie3 = "Matrix"
            year3 = 1999
    }

    @Unroll
    void "Return object"() {
        setup:
            mockDomain(TestDomain, [
                [id: 1, name: movie1, year: year1],
                [id: 2, name: movie2, year: year2],
                [id: 3, name: movie3, year: year3] ])
        when:
            def result = service.retrieveDomain(TestDomain, searchId)

        then:
            result != null
            result.name == searchMovie
            result.year == searchYear

        where:
            movie1 = "Lord of the Rings"
            year1 = 2001

            movie2 = "Star Wars"
            year2 = 1977

            movie3 = "Matrix"
            year3 = 1999

            searchId << [ 1, 2, 3 ]
            searchMovie << [ "Lord of the Rings", "Star Wars", "Matrix" ]
            searchYear << [ 2001, 1977, 1999 ]
    }

    void "Save object"() {
        setup:
            mockDomain(TestDomain)

        when:
            def result = service.saveDomain(TestDomain, ['name': 'Silence of the Lambs', 'year': 1991])
            def find = TestDomain.findByName('Silence of the Lambs')

        then:
            result != null
            find != null
            result.name == find.name
            result.year == find.year
    }

    void "Save object, fail validation"() {
        setup:
            mockDomain(TestDomain)

        when:
            def result = service.saveDomain(TestDomain, ['name': null, 'year': 1991])

        then:
            thrown(RuntimeException)
    }

    void "Update object"() {
        setup:
            mockDomain(TestDomain,[
                [id: 1, name: 'The Matrix', year: 2001]])

        when:
            def result = service.updateDomain(TestDomain, 1, ['year': 2014])
            def find = TestDomain.get(1)

        then:
            result != null
            find != null
            result.name == find.name
            result.year == find.year
    }

    void "Update object, object doesn't exist"() {
        setup:
            mockDomain(TestDomain,[
                [id: 1, name: 'The Matrix', year: 2001]])

        when:
            def result = service.updateDomain(TestDomain, 2, ['year': 2014])

        then:
            thrown(RuntimeException)
    }

    void "Update object, validation error"() {
        setup:
            mockDomain(TestDomain,[
                [id: 1, name: 'The Matrix', year: 2001]])

        when:
            def result = service.updateDomain(TestDomain, 1, ['name': null])

        then:
            thrown(RuntimeException)
    }

    void "Remove object"() {
        setup:
            mockDomain(TestDomain,[
                [id: 1, name: 'The Matrix', year: 2001]])

        when:
            def result = service.deleteDomain(TestDomain, 1)
            def find = TestDomain.findByName('The Matrix')

        then:
            find == null

    }
}

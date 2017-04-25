
package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import spock.lang.*
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import grails.util.Holders

@Mock([admin.test.TestEnumDomain])
class EnumWidgetSpec extends Specification {
    @Shared
    def slurper

    void setupSpec() {
        def parser = new org.cyberneko.html.parsers.SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        slurper = new XmlSlurper(parser)

        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            admin.test.TestEnumDomain.class,
        ] as Class[])
    }


    def "create enum widget"() {
        setup:
            def widget = new EnumWidget()
            widget.internalAttrs["domainClass"] = admin.test.TestEnumDomain
            widget.internalAttrs["propertyName"] = "type"
            widget.htmlAttrs["required"] = true

        when:
            def html = widget.render()
            def result =  slurper.parseText(html)

        then:
            result.BODY.SELECT.size() == 1
            result.BODY.SELECT.OPTION.size() == 3
    }

    def "create enum widget (optional)"() {
        setup:
            def widget = new EnumWidget()
            widget.internalAttrs["domainClass"] = admin.test.TestEnumDomain
            widget.internalAttrs["propertyName"] = "type"
            widget.htmlAttrs["required"] = false

        when:
            def html = widget.render()
            def result =  slurper.parseText(html)

        then:
            result.BODY.SELECT.size() == 1
            result.BODY.SELECT.OPTION.size() == 4
    }

    def "Value selected"() {
        setup:
            def widget = new EnumWidget()
            widget.internalAttrs["domainClass"] = admin.test.TestEnumDomain
            widget.internalAttrs["propertyName"] = "type"
            widget.value = admin.test.TypeEnum.OPEN

        when:
            def html = widget.render()
            def result =  slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 4
            result.BODY.SELECT.OPTION.find { it.@selected != null } != null
    }
    def "Not enum"() {
        setup:
            def widget = new EnumWidget()
            widget.internalAttrs["domainClass"] = admin.test.TestEnumDomain
            widget.internalAttrs["propertyName"] = "name"

        when:
            def html = widget.render()
            def result =  slurper.parseText(html)

        then:
            result.BODY.SELECT.size() == 0
            result.BODY.P.size() == 1
    }

    void 'update value'(){
        setup:
            def enumWidget = new EnumWidget(value:value)
            def testEnumDomain = new admin.test.TestEnumDomain()
            enumWidget.internalAttrs['domainObject'] = testEnumDomain
            enumWidget.internalAttrs["domainClass"] = admin.test.TestEnumDomain
            enumWidget.internalAttrs['propertyName'] = 'type'

        when:
            enumWidget.updateValue()
        then:
            testEnumDomain.type == result

        where:
            value << ["OPEN", "CLOSED", "PENDING"]
            result << [admin.test.TypeEnum.OPEN, admin.test.TypeEnum.CLOSED, admin.test.TypeEnum.PENDING]


    }


}

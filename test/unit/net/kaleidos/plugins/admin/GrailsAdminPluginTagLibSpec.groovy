package net.kaleidos.plugins.admin

import grails.test.mixin.TestFor
import spock.lang.*
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import admin.test.TestDomain
import grails.util.Holders

import net.kaleidos.plugins.admin.config.AdminConfigHolder
import net.kaleidos.plugins.admin.renderer.GrailsAdminPluginHtmlRendererService
import net.kaleidos.plugins.admin.widget.GrailsAdminPluginWidgetService
import net.kaleidos.plugins.admin.widget.Widget
import org.codehaus.groovy.grails.web.mapping.DefaultLinkGenerator
import org.codehaus.groovy.grails.core.io.DefaultResourceLocator
import org.springframework.core.io.FileSystemResource

@TestFor(GrailsAdminPluginTagLib)
class GrailsAdminPluginTagLibSpec extends Specification {
    @Shared
    def adminConfigHolder

    @Shared
    def slurper

    def setupSpec() {
        Holders.grailsApplication = new DefaultGrailsApplication()
        Holders.grailsApplication.configureLoadedClasses([
            admin.test.TestDomain.class,
            admin.test.TestOtherDomain.class,
        ] as Class[])

        Holders.config = new ConfigObject()
        Holders.config.grails.plugin.admin.domains = [ "admin.test.TestDomain" ]
        Holders.config.grails.plugin.admin.domain.TestDomain = {
            create includes: ['name']
            edit includes: ['name']
            list includes: ['name']
            widget 'name', 'net.kaleidos.plugins.admin.TestTaglibNameWidget'
        }

        adminConfigHolder = new AdminConfigHolder()
        adminConfigHolder.initialize()
    }

    void setup(){
        tagLib.grailsAdminPluginHtmlRendererService = new GrailsAdminPluginHtmlRendererService()
        tagLib.grailsAdminPluginHtmlRendererService.adminConfigHolder = adminConfigHolder
        tagLib.grailsAdminPluginHtmlRendererService.grailsAdminPluginWidgetService = new GrailsAdminPluginWidgetService()
        tagLib.grailsAdminPluginHtmlRendererService.grailsLinkGenerator = Mock(DefaultLinkGenerator)
        tagLib.grailsAdminPluginHtmlRendererService.grailsLinkGenerator.link(_) >> ""
        tagLib.grailsResourceLocator = Mock(DefaultResourceLocator)
        tagLib.grailsResourceLocator.getResourceForUri(_) >> new FileSystemResource("LICENSE")
        tagLib.adminConfigHolder = adminConfigHolder
    }

    void 'Edit form fields'(){
        setup:
            def object = new TestDomain()

        when:
            def html = applyTemplate('<gap:editFormFields object="${object}" editWidgetProperties="${[\'class\':\'form-control\']}"/>',
                ['object':object])
        then:
            html != null
            html.contains "TAGLIB_WIDGET"
    }

    void 'Create form fields'(){
        setup:
            def object = new TestDomain()

        when:
            def html = applyTemplate('<gap:createFormFields className="admin.test.TestDomain" editWidgetProperties="${[\'class\':\'form-control\']}"/>',
                ['object':object])
        then:
            html != null
            html.contains "TAGLIB_WIDGET"
    }

    void 'Widget before'(){
        setup:
            def object = new TestDomain()

        when:
            def html = applyTemplate('<gap:widgetBeforeForm className="admin.test.TestDomain"/>')
        then:
            html != null
            html.contains "BEFORE_TAGLIB_WIDGET"
    }


    void 'Widget after'(){
        setup:
            def object = new TestDomain()

        when:
            def html = applyTemplate('<gap:widgetAfterForm className="admin.test.TestDomain"/>')

        then:
            html != null
            html.contains "AFTER_TAGLIB_WIDGET"
    }

    void 'Render list header'(){
        setup:
            def object = new TestDomain()

        when:
            def html = applyTemplate('<gap:listTitles className="admin.test.TestDomain" />')

        then:
            html != null
            html.contains "name"
    }

    void 'Render list row'(){
        setup:
            def object = new TestDomain(name:"ROW")

        when:
            def html = applyTemplate('<gap:listLine object="${object}"/>', ["object":object])

        then:
            html != null
            html.contains "ROW"
    }

    void 'Layout CSS'(){
        when:
            def html = applyTemplate('<gap:layoutCss formType="create" className="admin.test.TestDomain"/>')

        then:
            html != null
    }

    void 'Layout JS'(){
        when:
            def html = applyTemplate('<gap:layoutJs formType="create" className="admin.test.TestDomain"/>')

        then:
            html != null
    }

    void 'Pagination for lists'() {
        setup:
            def object = new DomainInspector(new TestDomain())

        when:
            def html = applyTemplate('<gap:pagination domain="${domain}" totalPages="${' + totalPage + '}" currentPage="${' + totalPage + '}"/>', ['domain':object])

        then:
            html != null

        where:
            totalPage << [1,50,50,50,50,50,50,50,50,50,50]
            currentPage << [1,5,10,15,20,25,30,35,40,45,50]
    }
}

class TestTaglibNameWidget extends Widget{
    String render() {
        return "TAGLIB_WIDGET"
    }

    String renderBeforeForm() {
        return "BEFORE_TAGLIB_WIDGET"
    }

    String renderAfterForm() {
        return "AFTER_TAGLIB_WIDGET"
    }
}

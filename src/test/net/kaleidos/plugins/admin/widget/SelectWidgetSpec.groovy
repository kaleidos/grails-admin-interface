package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

// <select>
//     <option value="Volvo">Volvo</option>
//     <option value="Saab" selected="selected">Saab</option>
//     <option value="Opel">Opel</option>
//     <option value="Audi">Audi</option>
// </select>



class SelectWidgetSpec extends Specification {
    @Shared
    def slurper

    void setupSpec() {
        def parser = new org.cyberneko.html.parsers.SAXParser()
        parser.setFeature('http://xml.org/sax/features/namespaces', false)
        slurper = new XmlSlurper(parser)
    }

    void setup() {
        Object.metaClass.encodeAsHTML = {
            def encoder = new HTMLCodec().getEncoder()
            return encoder.encode(delegate)
        }
    }

    void 'create element without value without options without attribs'() {
        setup:
            def widget = new SelectWidget()
        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 1
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"
    }


    void 'create non nullable element without value without options without attribs'() {
        setup:
            def widget = new SelectWidget()
            widget.htmlAttrs.required = "true"
        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 0
            result.BODY.SELECT.@required.text() == "true"
    }


    void 'create element with value without options without attribs'() {
        setup:
            def widget = new SelectWidget(value:value)

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 1
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"

        where:
            value = "Saab"
    }

    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectWidget(internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 5
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"
            result.BODY.SELECT.OPTION[1].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[1].text() == "Volvo"
            result.BODY.SELECT.OPTION[2].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[2].text() == "Saab"
            result.BODY.SELECT.OPTION[3].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[3].text() == "Opel"
            result.BODY.SELECT.OPTION[4].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[4].text() == "Audi"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }




    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectWidget(value:value, internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 5
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"
            result.BODY.SELECT.OPTION[1].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[1].text() == "Volvo"
            result.BODY.SELECT.OPTION[2].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[2].text() == "Saab"
            result.BODY.SELECT.OPTION[3].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[3].text() == "Opel"
            result.BODY.SELECT.OPTION[4].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[4].text() == "Audi"

        where:
            value = "Saab"
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }



    void 'create element with value without options with attribs'() {
        setup:
            def widget = new SelectWidget(value:value, htmlAttrs:attrs)

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@name.text() == "selectName"
            result.BODY.SELECT.@disabled.text() == "true"
            result.BODY.SELECT.OPTION.size() == 1
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"

        where:
            value = "Saab"
            attrs = ['name':"selectName", 'disabled':true]
    }

    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectWidget(internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 5
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"
            result.BODY.SELECT.OPTION[1].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[1].text() == "Volvo"
            result.BODY.SELECT.OPTION[2].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[2].text() == "Saab"
            result.BODY.SELECT.OPTION[3].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[3].text() == "Opel"
            result.BODY.SELECT.OPTION[4].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[4].text() == "Audi"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }


    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectWidget(value:value, internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 5
            result.BODY.SELECT.OPTION[0].@value.text() == ""
            result.BODY.SELECT.OPTION[0].text() == "--"
            result.BODY.SELECT.OPTION[1].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[1].text() == "Volvo"
            result.BODY.SELECT.OPTION[2].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[2].text() == "Saab"
            result.BODY.SELECT.OPTION[3].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[3].text() == "Opel"
            result.BODY.SELECT.OPTION[4].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[4].text() == "Audi"

        where:
            value = "Saab"
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }



}

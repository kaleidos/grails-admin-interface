package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec

// <select multiple multiple>
//     <option value="Volvo">Volvo</option>
//     <option value="Saab" selected="selected">Saab</option>
//     <option value="Opel">Opel</option>
//     <option value="Audi">Audi</option>
// </select>


class SelectMultipleWidgetSpec extends Specification {
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
            def widget = new SelectMultipleWidget()

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 0
            result.BODY.SELECT.@multiple.size() == 1
    }

    void 'create non nullable element without value without options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget()
            widget.htmlAttrs.required = "true"

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 0
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.@required.text() == "true"
    }


    void 'create element with value without options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value:value)

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.OPTION.size() == 0
            result.BODY.SELECT.@multiple.size() == 1

        where:
            value = ["Saab"]
    }


    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.OPTION.size() == 4
            result.BODY.SELECT.OPTION[0].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[0].text() == "Volvo"
            result.BODY.SELECT.OPTION[1].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[1].text() == "Saab"
            result.BODY.SELECT.OPTION[2].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[2].text() == "Opel"
            result.BODY.SELECT.OPTION[3].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[3].text() == "Audi"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }




    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value:value, internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.OPTION.size() == 4
            result.BODY.SELECT.OPTION[0].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[0].text() == "Volvo"
            result.BODY.SELECT.OPTION[1].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[1].text() == "Saab"
            result.BODY.SELECT.OPTION[1].@selected.text() == "selected"
            result.BODY.SELECT.OPTION[2].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[2].text() == "Opel"
            result.BODY.SELECT.OPTION[3].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[3].text() == "Audi"

        where:
            value = ["Saab"]
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }

    void 'create element with multiple values with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value:value, internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.OPTION.size() == 4
            result.BODY.SELECT.OPTION[0].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[0].text() == "Volvo"
            result.BODY.SELECT.OPTION[1].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[1].text() == "Saab"
            result.BODY.SELECT.OPTION[1].@selected.text() == "selected"
            result.BODY.SELECT.OPTION[2].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[2].text() == "Opel"
            result.BODY.SELECT.OPTION[3].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[3].text() == "Audi"
            result.BODY.SELECT.OPTION[3].@selected.text() == "selected"

        where:
            value = ["Saab", "Audi"]
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }



    void 'create element with value without options with attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value:value, htmlAttrs:attrs)

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.@name.text() == "selectName"
            result.BODY.SELECT.@disabled.text() == "true"
            result.BODY.SELECT.OPTION.size() == 0

        where:
            value = ["Saab"]
            attrs = ['name':"selectName", 'disabled':true]
    }

    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.OPTION.size() == 4
            result.BODY.SELECT.OPTION[0].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[0].text() == "Volvo"
            result.BODY.SELECT.OPTION[1].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[1].text() == "Saab"
            result.BODY.SELECT.OPTION[2].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[2].text() == "Opel"
            result.BODY.SELECT.OPTION[3].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[3].text() == "Audi"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }


    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value:value, internalAttrs:[options:options])

        when:
            def html = widget.render()
            def result = slurper.parseText(html)

        then:
            result.BODY.SELECT.@multiple.size() == 1
            result.BODY.SELECT.OPTION.size() == 4
            result.BODY.SELECT.OPTION[0].@value.text() == "Volvo"
            result.BODY.SELECT.OPTION[0].text() == "Volvo"
            result.BODY.SELECT.OPTION[1].@value.text() == "Saab"
            result.BODY.SELECT.OPTION[1].text() == "Saab"
            result.BODY.SELECT.OPTION[1].@selected.text() == "selected"
            result.BODY.SELECT.OPTION[2].@value.text() == "Opel"
            result.BODY.SELECT.OPTION[2].text() == "Opel"
            result.BODY.SELECT.OPTION[3].@value.text() == "Audi"
            result.BODY.SELECT.OPTION[3].text() == "Audi"

        where:
            value = ["Saab"]
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }



}

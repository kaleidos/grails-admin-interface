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

        then:
            html == "<select multiple><option value=\"\">--</option></select>"
    }

    void 'create non nullable element without value without options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget()
            widget.attrs.required = "true"

        when:
            def html = widget.render()

        then:
            html == "<select multiple required=\"true\"></select>"
    }


    void 'create element with value without options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value, [:])

        when:
            def html = widget.render()

        then:
            html == "<select multiple><option value=\"\">--</option></select>"

        where:
            value = "Saab"
    }

    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(null, [options:options])

        when:
            def html = widget.render()

        then:
            html == "<select multiple><option value=\"\">--</option><option value=\"Volvo\">Volvo</option><option value=\"Saab\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }




    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value, [options:options])

        when:
            def html = widget.render()

        then:
            html == "<select multiple><option value=\"\">--</option><option value=\"Volvo\">Volvo</option><option value=\"Saab\" selected=\"selected\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            value = "Saab"
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }



    void 'create element with value without options with attribs'() {
        setup:
            def widget = new SelectMultipleWidget(value, attrs)

        when:
            def html = widget.render()

        then:
            html == "<select multiple name=\"selectName\" disabled=\"true\"><option value=\"\">--</option></select>"

        where:
            value = "Saab"
            attrs = ['name':"selectName", 'disabled':true]
    }

    void 'create element without value with options without attribs'() {
        setup:
            attrs.options = options
            def widget = new SelectMultipleWidget(null, attrs)

        when:
            def html = widget.render()

        then:
            html == "<select multiple name=\"selectName\" disabled=\"true\"><option value=\"\">--</option><option value=\"Volvo\">Volvo</option><option value=\"Saab\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
            attrs = ['name':"selectName", 'disabled':true]
    }


    void 'create element with value with options without attribs'() {
        setup:
            attrs.options = options
            def widget = new SelectMultipleWidget(value, attrs)

        when:
            def html = widget.render()

        then:
            html == "<select multiple name=\"selectName\" disabled=\"true\"><option value=\"\">--</option><option value=\"Volvo\">Volvo</option><option value=\"Saab\" selected=\"selected\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            value = "Saab"
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
            attrs = ['name':"selectName", 'disabled':true]
    }



}

package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

// <select>
//     <option value="Volvo">Volvo</option>
//     <option value="Saab" selected="selected">Saab</option>
//     <option value="Opel">Opel</option>
//     <option value="Audi">Audi</option>
// </select>

class SelectWidgetSpec extends Specification {
    void 'create element without value without options without attribs'() {
        setup:
            def widget = new SelectWidget()
        when:
            def html = widget.render()

        then:
            html == "<select></select>"
    }


    void 'create element with value without options without attribs'() {
        setup:
            def widget = new SelectWidget(value, null)

        when:
            def html = widget.render()

        then:
            html == "<select></select>"

        where:
            value = "Saab"
    }

    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectWidget(null, null, options)

        when:
            def html = widget.render()

        then:
            html == "<select><option value=\"Volvo\">Volvo</option><option value=\"Saab\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }


    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectWidget(value, null, options)

        when:
            def html = widget.render()

        then:
            html == "<select><option value=\"Volvo\">Volvo</option><option value=\"Saab\" selected=\"selected\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            value = "Saab"
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
    }



    void 'create element with value without options with attribs'() {
        setup:
            def widget = new SelectWidget(value, attrs)

        when:
            def html = widget.render()

        then:
            html == "<select name=\"selectName\" disabled=\"true\"></select>"

        where:
            value = "Saab"
            attrs = ['name':"selectName", 'disabled':true]
    }

    void 'create element without value with options without attribs'() {
        setup:
            def widget = new SelectWidget(null, attrs, options)

        when:
            def html = widget.render()

        then:
            html == "<select name=\"selectName\" disabled=\"true\"><option value=\"Volvo\">Volvo</option><option value=\"Saab\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
            attrs = ['name':"selectName", 'disabled':true]
    }


    void 'create element with value with options without attribs'() {
        setup:
            def widget = new SelectWidget(value, attrs, options)

        when:
            def html = widget.render()

        then:
            html == "<select name=\"selectName\" disabled=\"true\"><option value=\"Volvo\">Volvo</option><option value=\"Saab\" selected=\"selected\">Saab</option><option value=\"Opel\">Opel</option><option value=\"Audi\">Audi</option></select>"

        where:
            value = "Saab"
            options = ["Volvo":"Volvo", "Saab":"Saab", "Opel":"Opel", "Audi":"Audi"]
            attrs = ['name':"selectName", 'disabled':true]
    }



}

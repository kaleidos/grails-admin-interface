package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

// <select>
//     <option value="volvo">Volvo</option>
//     <option value="saab">Saab</option>
//     <option value="opel">Opel</option>
//     <option value="audi">Audi</option>
// </select>

class SelectWidgetSpec extends Specification {
    void 'create element without value nor attribs'() {
        setup:
            def widget = new SelectWidget()
        when:
            def html = widget.render()

        then:
            html == "<select></select>"
    }

    void 'create element with value without attribs'() {
        setup:
            def widget = new SelectWidget(value, null)

        when:
            def html = widget.render()

        then:
            html == "<select><option value=\"volvo\">Volvo</option><option value=\"saab\">Saab</option><option value=\"opel\">Opel</option><option value=\"audi\">Audi</option></select>"

        where:
            value = [volvo:"Volvo", saab:"Saab", opel:"Opel", audi:"Audi"]
    }

    void 'create element without value with attribs'() {
        setup:
            def widget = new SelectWidget(null, attrs)

        when:
            def html = widget.render()

        then:
            html == "<select name=\"selectName\" disabled=\"true\"></select>"

        where:
            attrs = ['name':"selectName", 'disabled':true]
    }


    void 'create element with value and attribs'() {
        setup:
            def widget = new SelectWidget(value, attrs)

        when:
            def html = widget.render()

        then:
            html == "<select name=\"selectName\" disabled=\"true\"><option value=\"volvo\">Volvo</option><option value=\"saab\">Saab</option><option value=\"opel\">Opel</option><option value=\"audi\">Audi</option></select>"
        where:
            value = [volvo:"Volvo", saab:"Saab", opel:"Opel", audi:"Audi"]
            attrs = ['name':"selectName", 'disabled':true]
    }
}

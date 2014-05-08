package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec



//value << ["1234", "<script>alert(0)</script>"]

class CheckboxInputWidgetSpec extends Specification {
	
	void setup() {
		Object.metaClass.encodeAsHTML = {
			def encoder = new HTMLCodec().getEncoder()
			return encoder.encode(delegate)
		}
	}
	
    void 'create input checkbox without value nor attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget()

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\"></input>"
    }

    @Unroll
    void 'create input checkbox with value and text:#text and without attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, null)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\" value=\"${value.encodeAsHTML()}\">${text?text.encodeAsHTML():''}</input>"

        where:
            value = "<script>alert(1234)</script>"
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create input checkbox without value and text:#text and without attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, null)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\">${text?text.encodeAsHTML():''}</input>"

        where:
            value = null
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create input checkbox with value and text:#text and with attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, attrs)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\">${text?text.encodeAsHTML():''}</input>"

        where:
            attrs = ['size':10, 'name': 'test']
            value = "<script>alert(1234)</script>"
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }

    @Unroll
    void 'create input checkbox without value and text:#text and with attribs'() {
        setup:
            def checkboxInputWidget = new CheckboxInputWidget(value, text, attrs)

        when:
            def html = checkboxInputWidget.render()

        then:
            html == "<input type=\"checkbox\" size=\"10\" name=\"test\">${text?text.encodeAsHTML():''}</input>"

        where:
            attrs = ['size':10, 'name': 'test']
            value = null
            text << ["el valor es 1234", null, "<script>alert(0)</script>"]
    }
}

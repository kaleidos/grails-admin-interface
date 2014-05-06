package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class NumberInputWidgetSpec extends Specification {
	
	void setup() {
		Object.metaClass.encodeAsHTML = {
			def encoder = new HTMLCodec().getEncoder()
			return encoder.encode(delegate)
		}
	}
	
    void 'create input number without value nor attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget()

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" />"
    }

    void 'create input number with value without attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(value)

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input number without value with attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(attrs)

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input number with value and attribs'() {
        setup:
            def numberInputWidget = new NumberInputWidget(value, attrs)

        when:
            def html = numberInputWidget.render()

        then:
            html == "<input type=\"number\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

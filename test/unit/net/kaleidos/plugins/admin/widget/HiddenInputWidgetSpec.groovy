package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class HiddenInputWidgetSpec extends Specification {
	
	void setup() {
		Object.metaClass.encodeAsHTML = {
			def encoder = new HTMLCodec().getEncoder()
			return encoder.encode(delegate)
		}
	}
	
    void 'create input hidden without value nor attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget()

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" />"
    }

    void 'create input hidden with value without attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(value)

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input hidden without value with attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(attrs)

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input hidden with value and attribs'() {
        setup:
            def hiddenInputWidget = new HiddenInputWidget(value, attrs)

        when:
            def html = hiddenInputWidget.render()

        then:
            html == "<input type=\"hidden\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

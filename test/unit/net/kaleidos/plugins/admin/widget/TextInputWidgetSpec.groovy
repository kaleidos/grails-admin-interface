package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class TextInputWidgetSpec extends Specification {
	
	void setup() {
		Object.metaClass.encodeAsHTML = {
			def encoder = new HTMLCodec().getEncoder()
			return encoder.encode(delegate)
		}
	}
	
    void 'create input text without value nor attribs'() {
        setup:
            def textInputWidget = new TextInputWidget()

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" />"
    }
    
    void 'create input text with value without attribs'() {
        setup:
            def textInputWidget = new TextInputWidget(value)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input text without value with attribs'() {
        setup:
            def textInputWidget = new TextInputWidget(attrs)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input text with value and attribs'() {
        setup:
            def textInputWidget = new TextInputWidget(value, attrs)

        when:
            def html = textInputWidget.render()

        then:
            html == "<input type=\"text\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

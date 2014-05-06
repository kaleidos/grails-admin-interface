package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class UrlInputWidgetSpec extends Specification {
	
	void setup() {
		Object.metaClass.encodeAsHTML = {
			def encoder = new HTMLCodec().getEncoder()
			return encoder.encode(delegate)
		}
	}
	
    void 'create input url without value nor attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget()

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" />"
    }

    void 'create input url with value without attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget(value)

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" value=\"${value.encodeAsHTML()}\" />"
        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create input url without value with attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget(attrs)

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" size=\"10\" name=\"test\" />"
        where:
            attrs = ['size':10, 'name': 'test']
    }


    void 'create input url with value and attribs'() {
        setup:
            def urlInputWidget = new UrlInputWidget(value, attrs)

        when:
            def html = urlInputWidget.render()

        then:
            html == "<input type=\"url\" value=\"${value.encodeAsHTML()}\" size=\"10\" name=\"test\" />"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['size':10, 'name': 'test']
    }
}

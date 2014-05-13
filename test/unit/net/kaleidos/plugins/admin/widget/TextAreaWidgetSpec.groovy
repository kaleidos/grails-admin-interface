package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class TextAreaWidgetSpec extends Specification {


    void setup() {
        Object.metaClass.encodeAsHTML = {
            def encoder = new HTMLCodec().getEncoder()
            return encoder.encode(delegate)
        }
    }

    void 'create text area without value nor attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget()

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea></textarea>"
    }

    void 'create text area with value without attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(value:value)

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea>${value.encodeAsHTML()}</textarea>"

        where:
            value = "<script>alert(1234)</script>"
    }

    void 'create text area without value with attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(htmlAttrs:attrs)

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea rows=\"4\" cols=\"50\"></textarea>"

        where:
            attrs = ['rows':4, 'cols':50]
    }


    void 'create text area with value and attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(value:value, htmlAttrs:attrs)

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea rows=\"4\" cols=\"50\">${value.encodeAsHTML()}</textarea>"
        where:
            value = "<script>alert(1234)</script>"
            attrs = ['rows':4, 'cols':50]
    }
}

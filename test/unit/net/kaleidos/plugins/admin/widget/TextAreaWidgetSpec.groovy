package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin

import spock.lang.*

class TextAreaWidgetSpec extends Specification {
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
            def textAreaWidget = new TextAreaWidget(value)

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea>${value}</textarea>"

        where:
            value = "1234"
    }

    void 'create text area without value with attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(attrs)

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea rows=\"4\" cols=\"50\"></textarea>"

        where:
            attrs = ['rows':4, 'cols':50]
    }


    void 'create text area with value and attribs'() {
        setup:
            def textAreaWidget = new TextAreaWidget(value, attrs)

        when:
            def html = textAreaWidget.render()

        then:
            html == "<textarea rows=\"4\" cols=\"50\">${value}</textarea>"
        where:
            value = "1234"
            attrs = ['rows':4, 'cols':50]
    }
}

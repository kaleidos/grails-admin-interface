package net.kaleidos.plugins.admin.widget

import grails.plugin.spock.*
import grails.test.mixin.*

import spock.lang.*

import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec


class WidgetSpec extends Specification {
    def 'Default renderBeforeForm'(){
        setup:
            def widget = new DefaultWidget()
        when:
            widget.renderBeforeForm()
        then:
            notThrown(Exception)
    }

    def 'Default renderAfterForm'(){
        setup:
            def widget = new DefaultWidget()
        when:
            widget.renderAfterForm()
        then:
            notThrown(Exception)
    }

    def 'Default getAssets'(){
        setup:
            def widget = new DefaultWidget()
        when:
            def result = widget.getAssets()
        then:
            result != null
    }

    def 'Default renderError'(){
        setup:
            def widget = new DefaultWidget()
        when:
            def result = widget.renderError(new Exception())
        then:
            result != null
    }
}

class DefaultWidget extends Widget {
    @Override
    String render() {
        return ""
    }
}

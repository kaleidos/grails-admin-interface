package net.kaleidos.plugins.admin

import java.util.regex.Pattern
// import org.springframework.security.access.annotation.Secured

// @Secured(['IS_AUTHENTICATED_REMEMBERED'])
class AdminController {
    def adminMethod() {
        log.debug ">> Execute: ${params}"
        render "OK ${params}"
    }
}

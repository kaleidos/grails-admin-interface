package net.kaleidos.plugins.admin

import java.util.regex.Pattern
// import org.springframework.security.access.annotation.Secured

// @Secured(['IS_AUTHENTICATED_REMEMBERED'])
class DashboardController {
    def index() {
        render "DASHBOARD"
    }
}

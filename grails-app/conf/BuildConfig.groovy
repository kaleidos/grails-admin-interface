grails.project.work.dir = 'target'
grails.project.docs.output.dir = 'docs/manual'
grails.project.dependency.resolver = "maven"

grails.project.fork = [
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolution = {
    inherits "global"
    log "warn"

    repositories {
        grailsCentral()
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        build 'org.apache.httpcomponents:httpcore:4.3.2'
        build 'org.apache.httpcomponents:httpclient:4.3.2'
        build 'org.apache.httpcomponents:httpmime:4.3.3'
        test 'nekohtml:nekohtml:1.9.6.2'
    }

    plugins {
        build ":rest-client-builder:1.0.3", { export = false }
        build ":release:3.0.1", { export = false }
        build ':coveralls:0.1.1', { export = false }
        test ":code-coverage:1.2.7", { export = false }
    }
}

coverage {
    exclusions = [
        "**/admin/test/**"
    ]
}

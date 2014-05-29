grails-admin
============
[![Build Status](https://travis-ci.org/grails-admin/grails-admin.svg?branch=master)](https://travis-ci.org/grails-admin/grails-admin)
[![Coverage Status](https://coveralls.io/repos/grails-admin/grails-admin/badge.png?branch=master)](https://coveralls.io/r/grails-admin/grails-admin?branch=master)


**An awesomic Grails Admin Plugin for Grails**

Please visit the complete documentation here: https://grails-admin.github.io/grails-admin/


## Installation

To install you should include in your `BuildConfig.groovy`

    plugins {
        runtime "\:admin\:<version>.<minorVersion>"
    }

It's recommended that you include a Spring Security Core plugin to secure the administration.

```
plugins {
    // Spring Security v1.X , example:
    compile "\:spring-security-core\:1.2.7.3"
}
```

```
plugins {
    // ... or v2.X , example:
    compile "\:spring-security-core\:2.0-RC3"
}
```

and edit your app `Config.groovy` and include the list of domains to start working with:
```
grails.plugin.admin.domains = [ "conferences.Talk", "conferences.Speaker" ]
```

You can also extend the default behaviour creating your own custom widgets.
See [Custom Widgets](https://grails-admin.github.io/grails-admin/guide/customWidgets.html) section to learn more about.

```
grails.plugin.admin.domain.Conference = {
    widget "coordinates", "sample.MapWidget", height:400, width:600
}
```

## Contribution 

To learn more about how to contribute to this project please visit [Contribution](https://grails-admin.github.io/grails-admin/guide/contributing.html) section.

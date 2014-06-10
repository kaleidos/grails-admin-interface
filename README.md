grails-admin-interface
============
[![Build Status](https://travis-ci.org/kaleidos/grails-admin-interface.svg?branch=master)](https://travis-ci.org/kaleidos/grails-admin-interface)
[![Coverage Status](https://coveralls.io/repos/kaleidos/grails-admin-interface/badge.png?branch=master)](https://coveralls.io/r/kaleidos/grails-admin-interface?branch=master)


**An awesomic Grails Admin Interface Plugin for Grails**

Please visit the complete documentation here: https://kaleidos.github.io/grails-admin-interface/

You can test a sample application here: http://grails-admin-interface-sample.herokuapp.com/

You can get the sample application source here: https://github.com/kaleidos/grails-admin-interface-sample


## Installation

To install you should include in your `BuildConfig.groovy`

    plugins {
        runtime ":admin-interface:<version>.<minorVersion>"
    }

It's recommended that you include a Spring Security Core plugin to secure the administration.

```
plugins {
    // Spring Security v1.X , example:
    compile ":spring-security-core:1.2.7.3"
}
```

```
plugins {
    // ... or v2.X , example:
    compile ":spring-security-core:2.0-RC3"
}
```

and edit your app `Config.groovy` and include the list of domains to start working with:
```
grails.plugin.admin.domains = [ "conferences.Talk", "conferences.Speaker" ]
```

You can also extend the default behaviour creating your own custom widgets.
See [Custom Widgets](https://kaleidos.github.io/grails-admin-interface/guide/customWidgets.html) section to learn more about.

```
grails.plugin.admin.domain.Conference = {
    widget "coordinates", "sample.MapWidget", height:400, width:600
}
```

## Contribution

To learn more about how to contribute to this project please visit [Contribution](https://kaleidos.github.io/grails-admin-interface/guide/contributing.html) section.

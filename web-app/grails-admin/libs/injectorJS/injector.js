(function() {
    'use strict';

    window.Injector = {};

    var FN_ARGS = /^function\s*[^\(]*\(\s*([^\)]*)\)/m;
    var FN_ARG_SPLIT = /,/;
    var FN_ARG = /^\s*(_?)(\S+?)\1\s*$/;
    var STRIP_COMMENTS = /((\/\/.*$)|(\/\*[\s\S]*?\*\/))/mg;

    Injector.dependencies = {};

    //code based on angularJs $inject
    Injector.annotate = function(fn) {
        if (!fn._inject) {
            var inject = [];
            var fnText = fn.toString().replace(STRIP_COMMENTS, '');
            var argDecl = fnText.match(FN_ARGS);
            var args = argDecl[1].split(FN_ARG_SPLIT);

            for (var i = 0, length = args.length; i < length; i += 1) {
                args[i].replace(FN_ARG, function(all, underscore, name){
                    inject.push(name);
                });
            }

            fn._inject = inject;
        }

        return fn._inject;
    };

    Injector.add = function(fnName, fn) {
        Injector.dependencies[fnName] = fn;
    };

    Injector.get = function(fnName) {
        return Injector.dependencies[fnName];
    };

    var _invoke = function(name, overwrites, self) {
        var fn = Injector.get(name);

        return Injector.invoke(fn, overwrites, self);
    };

    Injector.invoke = function(fn, overwrites, self) {
        var args = [],
        inject = Injector.annotate(fn),
        overwrite,
        fnDependency;

        if(!overwrites) {
            overwrites = {};
        }

        for(var i = 0, length = inject.length; i < length; i += 1) {
            overwrite = overwrites[inject[i]];

            if(overwrite) {
                fnDependency = Injector.invoke(overwrite, self);
            } else {
                fnDependency = _invoke(inject[i], overwrites, self);
            }

            args.push(fnDependency);
        }

        return fn.apply(self, args);
    };

    return Injector;
}());

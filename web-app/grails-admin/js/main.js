window.app = {};

app.module = function (dpName, dependencies, fn) {
    Injector.add(dpName, fn);
    fn._inject = dependencies;
};

app.service = app.module;
app.view = app.module;

app.findViews =  function (elm) {
    var views;
    if (elm) {
        views = elm.find('[view]');
    } else {
        views = $('[view]');
    }

    views.each(function () {
        var viewsName = $(this).attr('view').split(',');
        var elm = $(this);

        for (var i = 0; i < viewsName.length; i++) {
            var fn = Injector.get(viewsName[i].trim());

            Injector.invoke(fn, {$el: function () {
                return elm;
            }});
        }
    });
};

app.exec = function (moduleName) {
    var fn = Injector.get(moduleName);

    return Injector.invoke(fn);
};

// configuration in exec before init views
app.configure = {};
app.configure.fns = [];
app.configure.exec = function () {
    for (var i = 0; i < app.configure.fns.length; i++) {
        app.configure.fns[i]();
    }
};

app.configure.addConfiguration = function (fn) {
    app.configure.fns.push(fn);
};

// onload in exec after init views
app.load = {};
app.load.fns = [];
app.load.exec = function () {
    for (var i = 0; i < app.configure.fns.length; i++) {
        app.load.fns[i]();
    }
};

app.load.onLoad = function (fn) {
    app.load.fns.push(fn);
};

app.init = function () {
    app.configure.exec();
    app.findViews();
    app.load.exec();
};

app.service('templateService', [], function () {
    function compile (source, context) {
        var template = Handlebars.compile(source);

        return template(context);
    }

    return {
        compile: compile,
        get: function (templateName, context) {
            var source   = $("#" + templateName).html();

            return compile(source, context)
        }
    };
});

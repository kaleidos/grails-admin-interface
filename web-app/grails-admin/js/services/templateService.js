app.service('templateService', [], function () {
    return {
        get: function (templateName, context) {
            var source   = $("#" + templateName).html();
            var template = Handlebars.compile(source);

            return template(context);
        }
    };
});

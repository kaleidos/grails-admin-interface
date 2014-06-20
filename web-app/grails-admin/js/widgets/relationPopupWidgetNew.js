app.view('relationPopupWidgetNew', ['$el'], function ($el) {
    var form = $el.find('form');
    var saveButton = $el.find(".js-relation-popup-widget-new-save-action");

    function open () {
        var deferred = $.Deferred();
        form
            .off('grailsadmin:validated')
            .on('grailsadmin:validated', function (event, result) {
                form.trigger("reset");
                $el.modal('toggle');

                deferred.resolve(result.id, result.__text__);
            });

        return deferred.promise();
    }

    $el.on('grailsadmin:relationPopupWidgetNew', function (event, fn) {
        open().done(fn);
    });

    saveButton.on('click', function () {
        form.submit();
    });
});

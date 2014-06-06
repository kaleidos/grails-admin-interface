app.view('listView', ['$el'], function ($el) {
    "use strict";

    $el.on('click', 'td:not(.js-list-delete)', function (e) {
        var url = $(this).closest("tr").data('url');

        if (url) {
            window.location = url;
        }
    });

});

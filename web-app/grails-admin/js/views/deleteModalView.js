app.view('deleteModal', ['$el'], function ($el) {
    "use strict";

    $el.on('show.bs.modal', function (e) {
        var url = $(e.relatedTarget).data('url');

        if (url) {
            $(this).find('form').attr('action', url);
        }
    });
});

app.view('deleteModal', ['$el'], function ($el) {
    "use strict";

    $el.on('show.bs.modal', function (e) {
        var url = $(e.relatedTarget).data('url');

        if (url) {
            var ids = getCheckedIds();
            url = url.replace("0", ids.join());
            $(this).find('form').attr('action', url);
        }
    });


    function getCheckedIds(){
        return $('.js-list-delete:checked').map(function(){
            return $(this).data('element-id');
        }).get();
    }
});

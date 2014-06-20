app.view('relationPopupWidgetList', ['templateService', 'paginationService'], function (templateService, paginationService) {
    "use strict";

    var dialog;
    var obj = {};

    obj.items_by_page = 10;
    obj.url = {};
    obj.excludeValues = [];
    obj.pagination = {};
    obj.pagination.pages = 1;
    obj.pagination.currentPage = 1;

    function body (json) {
        var result = {};

        result.list = json;

        result.list = _.map(result.list, function (item) {
            item.exclude = _.indexOf(obj.excludeValues, item.id) !== -1;

            return item;
        });

        return templateService.get("grails-admin-list", result);
    }

    function load () {
        return $.when(
            $.getJSON(obj.url.list, {
                'items_by_page': obj.items_by_page,
                'page': obj.pagination.currentPage}),
            $.getJSON(obj.url.count)
        );
    }

    function setPage () {
        load().done(function (list, total) {
            obj.pagination.pages = Math.floor(total[0].total / obj.items_by_page);

            var html = body(list[0]);
            var pagination = paginationService.build(obj.pagination.currentPage, obj.pagination.pages);

            dialog.find('.modal-body').html(html + pagination);
        });
    }

    function createOpenModal(title, body, deferred) {
        var pagination = paginationService.build(obj.pagination.currentPage, obj.pagination.pages);
        var dialogId = "confirm-dialog-" + new Date().getTime();
        var dialogHtml = templateService.get("grails-admin-modal",
                                             {dialogId: dialogId,
                                              body: body,
                                              pagination: pagination,
                                              title: title});

        $("body").append(dialogHtml);

        dialog = $("#" + dialogId);

        dialog.on('hidden.bs.modal', function(){
            $(this).remove();
        });

        dialog.modal({"backdrop" : "true", "keyboard":"true"});

        dialog.find(".js-ok").on('click', function(){
            var selectedItem = dialog.find('.relationpopup-radio:checked');

            if (selectedItem.length) {
                var objectId = selectedItem.val();
                var objectText = selectedItem.data('txt');

                deferred.resolve(objectId, objectText);
            }
        });

        dialog.on('click', '.pagination a', function (e) {
            e.preventDefault();

            var target = $(e.currentTarget);
            obj.pagination.currentPage = parseInt(target.data('page'), 10);
            setPage();
        });
    }

    function open (title, excludeValues, url_list, url_count) {
        var deferred = $.Deferred();

        obj.url.list = url_list;
        obj.url.count = url_count;
        obj.excludeValues = excludeValues;

        load().done(function (list, total) {
            var html = body(list[0]);
            obj.pagination.pages = Math.floor(total[0].total / obj.items_by_page);

            createOpenModal(title, html, deferred);
        });

        return deferred.promise();
    }

    return {
        'open': open
    }
});

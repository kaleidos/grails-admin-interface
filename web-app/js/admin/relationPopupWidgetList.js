app.view('relationPopupWidgetList', ['templateService'], function (templateService) {
    "use strict";

    var dialog;
    var obj = {};
    obj.items_by_page = 10;
    obj.url = {};
    obj.pages = 1;
    obj.excludeValues = [];
    obj.currentPage = 1;

    function buildPagination () {
        var max_left_right = 3;

        var leftPoints = obj.currentPage - max_left_right - 1
        var rightPoints = obj.currentPage + max_left_right + 1;
        var result = [];

        for (var i = 1; i <= obj.pages; i++) {
            if (i > leftPoints && i < rightPoints) {
                result.push({'text': i, 'page': i, 'active': i === obj.currentPage});
            } else if (i === leftPoints) {
                result.push({'text': 1, 'page': 1});

                if (1 !== leftPoints) {
                    result.push({'text': '...', 'disabled': true});
                }
            } else if(i === rightPoints) {
                if (rightPoints !== obj.pages) {
                    result.push({'text': '...', 'disabled': true});
                }

                result.push({'text': obj.pages, 'page': obj.pages});
            }
        }

        return templateService.get('grails-admin-pagination', result);
    }

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
                'page': obj.currentPage}),
            $.getJSON(obj.url.count)
        );
    }

    function setPage () {
        load().done(function (list, total) {
            obj.pages = Math.floor(total[0].total / obj.items_by_page);

            var html = body(list[0]);
            var pagination = buildPagination();

            dialog.find('.modal-body').html(html + pagination);
        });
    }

    function createOpenModal(title, body, deferred) {
        var pagination = buildPagination();
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
            obj.currentPage = parseInt(target.data('page'), 10);
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
            obj.pages = Math.floor(total[0].total / obj.items_by_page);

            createOpenModal(title, html, deferred);
        });

        return deferred.promise();
    }

    return {
        'open': open
    }
});

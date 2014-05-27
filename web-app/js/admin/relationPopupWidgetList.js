app.view('relationPopupWidgetList', ['templateService'], function (templateService) {
    "use strict";

    function body (json, excludeValues) {
        excludeValues = excludeValues || [];

        var items = _.map(json, function (item) {
            item.exclude = _.indexOf(excludeValues, item.id) !== -1;

            return item;
        });

        return templateService.get("grails-admin-list", items);
    }

    function createOpenModal(title, body, deferred){
        var dialogId = "confirm-dialog-" + new Date().getTime();
        var dialogHtml = templateService.get("grails-admin-modal",
                                             {dialogId: dialogId,
                                              body: body,
                                              title: title})

        $("body").append(dialogHtml);
        var dialog = $("#" + dialogId);


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
    }

    function open (json, excludeValues, title) {
        title = title || "Select";

        var deferred = $.Deferred();
        var html = body(json, excludeValues);

        createOpenModal(title, html, deferred);

        return deferred.promise();
    }

    return {
        'open': open
    }
});

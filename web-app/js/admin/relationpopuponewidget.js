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
        var dialog = templateService.get("grails-admin-modal", {dialogId: dialogId, body: body, title: title})

        $("body").append(dialog);
        $("#" + dialogId).on('hidden.bs.modal', function(){
            $("#" + dialogId).remove();
        });

        $("#" + dialogId).find(".js-ok").on('click', function(){
            var selectedItem = $('.relationpopup-radio:checked');

            if (selectedItem !== undefined) {
                var objectId = selectedItem.val();
                var objectText = selectedItem.data('txt');

                deferred.resolve(objectId, objectText);
            }
        });

        $("#" + dialogId).modal({"backdrop" : "true", "keyboard":"true"});
    }

    function open (json, excludeValues) {
        var deferred = $.Deferred();
        var html = body(json, excludeValues);

        createOpenModal("Select", html, deferred);

        return deferred.promise();
    }

    return {
        'open': open
    }
});

app.view('relationPopupOneWidgetNew', ['$el'], function ($el) {
    var form = $el.find('form');
    var saveButton = $el.find(".js-relationtablewidget-save-action");

    function open () {
        var deferred = $.Deferred();

        form.off('grailsadmin:validated');
        form.on('grailsadmin:validated', function (event, result) {
            form.trigger("reset");
            $el.modal('toggle');

            deferred.resolve(result.id, result.__text__);
        });

        return deferred.promise();
    }

    $el.on('grailsadmin:relationPopupOneWidgetNew', function (event, fn) {
        open().done(fn);
    });

    saveButton.on('click', function () {
        form.submit();
    });
});

app.view('relationPopupOneWidgetField', ['$el', 'relationPopupWidgetList'], function ($el, relationPopupWidgetList) {
    "use strict";

    function setValue (objectId, objectText, show) {
        $el.find(".js-one-rel-value").val(objectId);
        $el.find(".js-one-rel-text").text(objectText);

        if (show) {
            $el.find(".js-relationpopuponewidget-delete").show();
        } else {
            $el.find(".js-relationpopuponewidget-delete").hide();
        }
    }

    function addOneElement (objectId, objectText) {
        setValue(objectId, objectText, true);
    }

    function removeElement () {
        setValue(null, "<< empty >>", false);
    }

    function openNewPopup () {
        app.getView('relationPopupOneWidgetNew').trigger('grailsadmin:relationPopupOneWidgetNew', addOneElement);
    }

    function openListPopup () {
        var input = $el.find(".js-one-rel-value");
        var currentValue = [];

        if (input.length) {
            currentValue.push(parseInt(input.val()));
        }

        $.getJSON($(this).data('url'))
            .done(function (result) {
                relationPopupWidgetList
                    .open(result, currentValue)
                    .done(addOneElement)
            })
            .fail(function (result) {
                $el.find(".modal-body").html("ERROR");
            });
    }

    $el.find(".js-relationpopuponewidget-new").on('click', openNewPopup);
    $el.find(".js-relationpopuponewidget-list").on('click', openListPopup);
    $el.find(".js-relationpopuponewidget-delete").on('click', removeElement);
});

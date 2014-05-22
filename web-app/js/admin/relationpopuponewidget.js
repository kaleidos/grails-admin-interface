app.service('templateService', [], function () {
    return {
        get: function (templateName, context) {
            var source   = $("#" + templateName).html();
            var template = Handlebars.compile(source);

            return template(context);
        }
    };
});

app.view('relationPopupWidgetList', ['templateService'], function (templateService) {
    "use strict";

    function body (json, excludeValues) {
        excludeValues = excludeValues || [];

        var content = _.filter(json, function (item) {
            return _.indexOf(item.id, excludeValues) !== -1;
        });

        return templateService.get("grails-admin-list", content);
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


    // var form = $el.find('form');


    // var saveButton = $el.find(".js-relationtablewidget-save-action");

    // saveButton.off('click');
    // saveButton.on('click', function(){
    //     event.preventDefault();
    //     var modal = $(this).parents(".modal");
    //     var form = modal.find("form");
    //     var field = modal.data("field");

    //     form.off('grailsadmin:validated');
    //     form.on('grailsadmin:validated', function (event, result) {
    //         form.trigger("reset");

    //         modal.modal('toggle');

    //         $el.find(".js-one-rel-value[name='" + field  + "']").val(result['id']);
    //         $el.find(".js-one-rel-text[name='" + field  + "']").text(result['__text__']);
    //     });

    //     form.submit();
    // });

    return {
        'open': open
    }
});

app.view('relationPopupOneWidgetField', ['$el', 'relationPopupWidgetList'], function ($el, relationPopupWidgetList) {
    "use strict";

    function addOneElement (objectId, objectText) {
        $el.find(".js-one-rel-value").val(objectId);
        $el.find(".js-one-rel-text").text(objectText);
        $(".js-relationpopuponewidget-delete").show();
    }

    function removeElementCallBack () {
        $el.find(".js-one-rel-value").val(null);
        $el.find(".js-one-rel-text").text("<< empty >>");
    }

    function openListPopup () {
        var input = $el.find(".js-one-rel-value");
        var currentValue = [];

        if (input.lenght) {
            currentValue.push(parseInt(input.val()));
        }

        $.getJSON($(this).data('url'))
            .done(function (result) {
                relationPopupWidgetList.open(result, [currentValue])
                    .done(addOneElement)
            })
            .fail(function (result) {
                $el.find(".modal-body").html("ERROR");
            });
    }

    function removeElement () {
        event.preventDefault();
        removeElementCallBack();
        $el.find(".js-relationpopuponewidget-delete").hide();
    }

    $el.find(".js-relationpopuponewidget-list").on('click', openListPopup);
    $el.find(".js-relationpopuponewidget-delete").on('click', removeElement);
});

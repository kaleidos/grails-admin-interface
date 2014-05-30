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

    function openNewPopup (event) {
        var target = $(event.currentTarget).data('target');

        $(target).trigger('grailsadmin:relationPopupWidgetNew', addOneElement);
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
                alert("ERROR");
            });
    }

    $el.find(".js-relationpopuponewidget-new").on('click', openNewPopup);
    $el.find(".js-relationpopuponewidget-list").on('click', openListPopup);
    $el.find(".js-relationpopuponewidget-delete").on('click', removeElement);
});

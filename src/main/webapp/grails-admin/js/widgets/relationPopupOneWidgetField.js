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

    function removeElement (event) {
        event.preventDefault();
        setValue(null, "<< empty >>", false);
    }

    function openNewPopup (event) {
        var target = $(event.currentTarget).data('target');

        $(target).trigger('grailsadmin:relationPopupWidgetNew', addOneElement);
    }

    function openListPopup (page) {
        page = page || 0;

        var input = $el.find(".js-one-rel-value");
        var currentValue = [];

        if (input.length) {
            currentValue.push(parseInt(input.val()));
        }

        var url_list = $(this).data('url');
        var url_count = $(this).data('url-count');

        relationPopupWidgetList
            .open("Select", [currentValue], url_list, url_count)
            .done(addOneElement);
    }

    $el.find(".js-relationpopuponewidget-new").on('click', openNewPopup);
    $el.find(".js-relationpopuponewidget-list").on('click', openListPopup);
    $el.find(".js-relationpopuponewidget-delete").on('click', removeElement);
});

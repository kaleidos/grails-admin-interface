app.view('relationtablewidget', ['$el'], function ($el) {
    "use strict";

    function addItem (objectId, objectText){
        var selectedItem = $('.relationpopup-radio:checked');
        if (selectedItem !== undefined) {
            var val = selectedItem.val();
            var txt = selectedItem.data('txt');
            var detailUrl = $el.find("table").data('detailurl');
            var propertyName = $el.find("table").data('property-name');
            detailUrl = detailUrl.replace("0", objectId);
            var optional = $el.find("table").data('optional');
            var newLine = createRelationTableWidgetLine(detailUrl, objectId, objectText, optional);
            var table = $el.find("tbody");

            if (table.size() == 0) {
                table = $el.find("table");

            }
            table.append(newLine);

        if (!table.length) {
            table = $el.find("table");
        }
    }

    function createRelationTableWidgetLine (detailUrl, val, txt, optional) {
        var line = "<tr><td><a href=\"" + detailUrl + "\">" + escape(txt) + "</a></td>";

        if (optional) {
            line += "<td class=\"list-actions\"><a class=\"btn btn-default btn-sm js-relationtablewidget-delete\" data-value=\"" + htmlEncode(val) + "\" href=\"#\"><span class=\"glyphicon glyphicon-trash\"></span> Delete</a></td>"
        }
        line += "</tr>"
        return line;
    }

    function deleteRelation (event) {
        event.preventDefault();
        var r = confirm( "Do you wish to delete the relation?" );
        if (r == true) {
            $el.find("input[type='hidden'][value=" + $(this).data('value') + "]").remove();
            $(this).closest( "tr" ).remove()

        }
    }

    function addRelation (event) {
        var target = $(this).data("target");

        $.getJSON($(this).data('url'))
        .done(function (result) {
            var excludeValues = $el.find("input[type='hidden']").map(function(i,v) {
                return $(this).val();
            }).toArray();

            var html = relationPopupCreateBody(result, excludeValues);
            relationPopupOpenConfirmDialog("Add", html, addItem);
        })
        .fail(function (result) {
            alert("ERROR");
        });
    }

    $el.on( "click", ".js-relationtablewidget-delete", deleteRelation);
    $el.find( ".js-relationtablewidget-add").on("click", addRelation);
});

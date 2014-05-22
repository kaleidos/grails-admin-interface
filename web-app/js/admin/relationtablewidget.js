app.view('relationtablewidget', ['$el'], function ($el) {
    "use strict";

    $el.on( "click", ".js-relationtablewidget-delete", function(event) {
        event.preventDefault();
        var r = confirm( "Do you wish to delete the relation?" );
        if (r == true) {
            $el.find("input[type='hidden'][value=" + $(this).data('value') + "]").remove();
            $(this).closest( "tr" ).remove()

        }
    });


    $el.find( ".js-relationtablewidget-add").click(function(event) {
        var target = $(this).data("target");

        $.ajax({
            method: "GET",
            url: $(this).data('url'),
            dataType: "JSON",
            contentType: 'application/json; charset=utf-8',
        })
        .done(function (result) {
            var excludeValues = $el.find("input[type='hidden']").map(function(i,v) {
                return $(this).val();
            }).toArray();

            var html = relationPopupCreateBody(result, excludeValues);
            relationPopupOpenConfirmDialog("Add", html, $el.addItem);
        })
        .fail(function (result) {
            alert("ERROR");
        });

    });


    $el.addItem = function(objectId, objectText){
        var detailUrl = $el.find("table").data('detailurl');
        var propertyName = $el.find("table").data('property-name');
        detailUrl = detailUrl.replace("0", objectId);
        var optional = $el.find("table").data('optional');

        var newLine = $el.createRelationTableWidgetLine(detailUrl, objectId, objectText, optional);
        var table = $el.find("tbody");

        if (!table.length) {
            table = $el.find("table");
        }
        table.append(newLine);

        $el.prepend("<input type=\"hidden\" name=\"" + propertyName + "\"  value=\"" + htmlEncode(objectId) + "\" />")
    };

    $el.createRelationTableWidgetLine = function (detailUrl, val, txt, optional) {
        var line = "<tr><td><a href=\"" + detailUrl + "\">" + htmlEncode(txt) + "</a></td>";
        if (optional) {
            line += "<td class=\"list-actions\"><a class=\"btn btn-default btn-sm js-relationtablewidget-delete\" data-value=\"" + htmlEncode(val) + "\" href=\"#\"><span class=\"glyphicon glyphicon-trash\"></span> Delete</a></td>"
        }
        line += "</tr>"
        return line;
    };
});

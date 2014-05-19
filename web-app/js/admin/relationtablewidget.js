$( ".relationtablewidget" ).on( "click", ".js-relationtablewidget-delete", function(event) {
    event.preventDefault();
    var r = confirm( "Do you wish to delete the relation?" );
    if (r == true) {
        $(this).closest( ".relationtablewidget" ).find("input[type='hidden'][value=" + $(this).data('value') + "]").remove();
        $(this).closest( "tr" ).remove()

    }
});


$( ".js-relationtablewidget-add").click(function(event) {
    var target = $(this).data("target");

    $.ajax({
        method: "GET",
        url: $(this).data('url'),
        dataType: "JSON",
        contentType: 'application/json; charset=utf-8',
    })
    .done(function (result) {
        var html = createRelationTableWidgetPopupBody(result)
        $(target).find(".modal-body").html(html);
    })
    .fail(function (result) {
        $(target).find(".modal-body").html("ERROR");
    });

});


$( ".js-relationtablewidget-add-action").click(function(event) {
    var selectedItem = $(this).closest('.modal-content').find('.relationtablewidget-radio:checked');
    if (selectedItem !== undefined) {
        var val = selectedItem.val();
        var txt = selectedItem.data('txt');
        var detailUrl = $(".relationtablewidget").find("table").data('detailurl');
        var propertyName = $(".relationtablewidget").find("table").data('property-name');
        detailUrl = detailUrl.replace("0", val);
        var optional = $(".relationtablewidget").find("table").data('optional');

        var newLine = createRelationTableWidgetLine(detailUrl, val, txt, optional);
        $(".relationtablewidget").find("tbody").append(newLine)

        $(".relationtablewidget").prepend("<input type=\"hidden\" name=\"" + propertyName + "\"  value=\"" + escape(val) + "\" />")
    }


});


function createRelationTableWidgetPopupBody(json) {
    var html = "<table class=\"table table-bordered\">";
    html += createRelationTableWidgetPopupHeader(json[0]);
    $.each( json, function( key, value ) {
        html += createRelationTableWidgetPopupLine(value);
    });
    html += "</table>";
    return html;
}


function createRelationTableWidgetPopupHeader(json) {
    var line = "<tr><th>&nbsp;</th>";

    $.each( json, function( key, value ) {
        if (key.lastIndexOf("_", 0) !== 0) {
            line += "<th>" + escape(key) + "</th>";
        }
    });
    line += "</tr>";
    return line;
}


function createRelationTableWidgetPopupLine(json) {
    var line = "<tr><td><input class=\"relationtablewidget-radio\" type=\"radio\" name=\"elementId\" value=\"" + escape(json['id']) + "\" data-txt=\"" + escape(json['__text__']) + "\"></input></td>";
    $.each( json, function( key, value ) {
        if (key.lastIndexOf("_", 0) !== 0) {
            line += "<td>" + escape(value) + "</td>";
        }
    });
    line += "</tr>";
    return line;
}


function createRelationTableWidgetLine(detailUrl, val, txt, optional) {
    var line = "<tr><td><a href=\"" + detailUrl + "\">" + escape(txt) + "</a></td>";
    if (optional) {
        line += "<td><a class=\"btn btn-default btn-sm js-relationtablewidget-delete\" data-value=\"" + escape(val) + "\" href=\"#\"><span class=\"glyphicon glyphicon-trash\"></span> Delete</a></td>"
    }
    line += "</tr>"
    return line;
}
